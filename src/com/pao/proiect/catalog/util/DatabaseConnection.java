package com.pao.proiect.catalog.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton pentru conexiunea la baza de date.
 * Citeste configuratia din resources/db.properties de pe classpath.
 */
public final class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws IOException, SQLException {
        Properties props = new Properties();
        try (InputStream is = getClass().getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (is == null) {
                throw new IOException(
                        "db.properties nu a fost gasit pe classpath. " +
                        "Adauga 'resources/' ca Sources Root in IDE.");
            }
            props.load(is);
        }
        String url  = props.getProperty("db.url");
        String user = props.getProperty("db.user", "");
        String pass = props.getProperty("db.password", "");

        // incarca explicit driverul SQLite
        try { Class.forName("org.sqlite.JDBC"); } catch (ClassNotFoundException ignored) {}

        this.connection = DriverManager.getConnection(url, user, pass);

        // activeaza FK-uri in SQLite
        try (var st = connection.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        }
    }

    public static synchronized DatabaseConnection getInstance()
            throws IOException, SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
