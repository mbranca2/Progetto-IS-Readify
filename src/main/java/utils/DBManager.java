package utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBManager {

    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/readify?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL non trovato", e);
        }

        Properties props = new Properties();
        try (InputStream in = DBManager.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (Exception e) {
            throw new RuntimeException("Impossibile leggere db.properties", e);
        }

        URL = firstNonBlank(
                System.getenv("READIFY_DB_URL"),
                System.getProperty("readify.db.url"),
                props.getProperty("db.url"),
                DEFAULT_URL
        );

        USER = firstNonBlank(
                System.getenv("READIFY_DB_USER"),
                System.getProperty("readify.db.user"),
                props.getProperty("db.user"),
                DEFAULT_USER
        );

        PASSWORD = firstNonBlank(
                System.getenv("READIFY_DB_PASSWORD"),
                System.getProperty("readify.db.password"),
                props.getProperty("db.password"),
                DEFAULT_PASSWORD
        );
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static String firstNonBlank(String... values) {
        for (String v : values) {
            if (v != null && !v.trim().isEmpty()) {
                return v.trim();
            }
        }
        return null;
    }
}

