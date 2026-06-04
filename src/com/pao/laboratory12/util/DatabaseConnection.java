package com.pao.laboratory12.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws IOException, SQLException {
        String url  = "jdbc:sqlite:lab12.db";
        String user = "";
        String pass = "";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                url  = props.getProperty("db.url", url);
                user = props.getProperty("db.user", "");
                pass = props.getProperty("db.password", "");
            }
        }
        try { Class.forName("org.sqlite.JDBC"); } catch (ClassNotFoundException ignored) {}
        this.connection = DriverManager.getConnection(url, user, pass);
        try (var stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException ignored) {}
    }

    public static synchronized DatabaseConnection getInstance() throws IOException, SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() { return connection; }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) connection.close();
    }
}
