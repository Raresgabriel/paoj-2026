package com.pao.laboratory14.exercise2.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton care gestioneaza conexiunea la baza de date.
 * Citeste configuratia din db.properties de pe classpath.
 *
 * Configurare IntelliJ: marcheaza 'exercise2/resources/' ca Resources Root.
 */
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws IOException, SQLException {
        String url  = "jdbc:sqlite:output/lab14_ex2.db";
        String user = "";
        String pass = "";

        // incearca sa citeasca db.properties din classpath (merge din terminal cu -cp resources/)
        // daca nu e gasit (ex: IntelliJ fara Resources Root configurat) foloseste fallback SQLite
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

        // asigura-te ca directorul output/ exista
        new java.io.File("output").mkdirs();

        this.connection = DriverManager.getConnection(url, user, pass);
    }

    public static DatabaseConnection getInstance() throws IOException, SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}

