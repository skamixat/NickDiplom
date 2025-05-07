package org.nikita.armaplus2;

import java.sql.*;

public class Database {
    private static Connection connection;

    public static void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:finance.db");
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS operations (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        type TEXT NOT NULL,
                        amount REAL NOT NULL,
                        date TEXT NOT NULL,
                        description TEXT
                    )
                """);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void disconnect() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
