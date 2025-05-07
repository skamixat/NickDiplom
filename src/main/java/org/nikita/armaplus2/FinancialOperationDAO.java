package org.nikita.armaplus2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FinancialOperationDAO {
    public static void insert(FinancialOperation op) {
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(
                "INSERT INTO operations (type, amount, date, description) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, op.getType());
            stmt.setDouble(2, op.getAmount());
            stmt.setString(3, op.getDate());
            stmt.setString(4, op.getDescription());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<FinancialOperation> getAll() {
        List<FinancialOperation> list = new ArrayList<>();
        try (Statement stmt = Database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM operations")) {
            while (rs.next()) {
                list.add(new FinancialOperation(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getString("date"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
