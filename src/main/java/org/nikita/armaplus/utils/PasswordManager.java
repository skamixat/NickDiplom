package org.nikita.armaplus.utils;

import org.nikita.armaplus.database.DatabaseHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PasswordManager {

    private final DatabaseHandler databaseHandler;

    public PasswordManager(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public boolean verifyPassword(String role, String password) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = databaseHandler.getConnection();
            String query = "SELECT password FROM users WHERE role = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, role);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("password").equals(password);
            }
            return false;
        } finally {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) databaseHandler.closeConnection(connection);
        }
    }
}