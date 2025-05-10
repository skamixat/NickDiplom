package org.nikita.armaplus.controllers;

import org.nikita.armaplus.MainApplication;
import org.nikita.armaplus.database.DatabaseHandler;
import org.nikita.armaplus.database.model.IncomeExpense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdminController {

    @FXML
    private TableView<IncomeExpense> incomeExpenseTable;

    @FXML
    private TableColumn<IncomeExpense, String> typeColumn;

    @FXML
    private TableColumn<IncomeExpense, LocalDate> dateColumn;

    @FXML
    private TableColumn<IncomeExpense, String> expenseTypeColumn;

    @FXML
    private TableColumn<IncomeExpense, Double> amountColumn;

    @FXML
    private Button backButton;

    private final DatabaseHandler databaseHandler;
    private final ObservableList<IncomeExpense> incomeExpenseList = FXCollections.observableArrayList();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public AdminController() {
        databaseHandler = new DatabaseHandler();
    }

    @FXML
    public void initialize() {
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        expenseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("expenseTypeName"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        loadIncomeExpenses();
        incomeExpenseTable.setItems(incomeExpenseList);
    }

    private void loadIncomeExpenses() {
        incomeExpenseList.clear();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = databaseHandler.getConnection();
            String query = "SELECT ie.id, ie.type, ie.date, et.type_name, ie.amount " +
                    "FROM income_expenses ie " +
                    "JOIN expense_types et ON ie.expense_type_id = et.id";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String type = resultSet.getString("type");
                LocalDate date = LocalDate.parse(resultSet.getString("date"));
                String expenseTypeName = resultSet.getString("type_name");
                double amount = resultSet.getDouble("amount");
                incomeExpenseList.add(new IncomeExpense(id, type, date, 0, expenseTypeName, amount)); // expenseTypeId здесь не нужен для отображения
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Обработка ошибки
        } finally {
            databaseHandler.closeConnection(connection);
            try {
                if (statement != null) statement.close();
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("login-view.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Обработка ошибки загрузки
        }
    }
}