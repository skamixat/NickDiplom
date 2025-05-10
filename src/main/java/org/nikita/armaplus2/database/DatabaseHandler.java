package org.nikita.armaplus2.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {

    private static final String DB_URL = "jdbc:sqlite:database.db";

    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL);
        // Включаем foreign key constraints для SQLite
        Statement statement = connection.createStatement();
        statement.executeUpdate("PRAGMA foreign_keys = ON;");
        statement.close();
        return connection;
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}