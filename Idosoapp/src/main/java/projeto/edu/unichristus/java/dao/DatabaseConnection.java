package projeto.edu.unichristus.java.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/idosoapp";
    private static final Properties PROPERTIES = loadProperties();

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String url = read("DB_URL", "db.url", DEFAULT_URL);
        String user = read("DB_USER", "db.user", "root");
        String password = read("DB_PASSWORD", "db.password", "");
        return DriverManager.getConnection(url, user, password);
    }

    public static Integer generatedId(PreparedStatement statement) throws SQLException {
        try (ResultSet rs = statement.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return null;
    }

    private static String read(String envName, String propertyName, String fallback) {
        String env = System.getenv(envName);
        if (env != null && !env.trim().isEmpty()) {
            return env;
        }
        String property = PROPERTIES.getProperty(propertyName);
        if (property != null && !property.trim().isEmpty()) {
            return property;
        }
        return fallback;
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (Exception e) {
            System.err.println("Nao foi possivel carregar application.properties: " + e.getMessage());
        }
        return properties;
    }
}
