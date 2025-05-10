package org.nikita.armaplus.controllers;


import org.nikita.armaplus.MainApplication;
import org.nikita.armaplus.database.DatabaseHandler;
import org.nikita.armaplus.database.model.FinishedProduct;
import org.nikita.armaplus.database.model.RawMaterial;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class WarehouseController {

    @FXML
    private TableView<RawMaterial> rawMaterialsTable;
    @FXML
    private TableColumn<RawMaterial, Integer> rawMaterialIdColumn;
    @FXML
    private TableColumn<RawMaterial, Double> tonsPurchasedColumn;
    @FXML
    private TableColumn<RawMaterial, Double> purchaseAmountColumn;
    @FXML
    private TableColumn<RawMaterial, Double> logisticsCostRawColumn;
    @FXML
    private TableColumn<RawMaterial, LocalDate> purchaseDateColumn;
    @FXML
    private TableColumn<RawMaterial, String> expenseTypeRawColumn;
    @FXML
    private Button addRawMaterialButton;

    @FXML
    private TableView<FinishedProduct> finishedProductsTable;
    @FXML
    private TableColumn<FinishedProduct, Integer> finishedProductIdColumn;
    @FXML
    private TableColumn<FinishedProduct, Double> tonsProducedColumn;
    @FXML
    private TableColumn<FinishedProduct, Double> soldAmountColumn;
    @FXML
    private TableColumn<FinishedProduct, Double> logisticsCostFinishedColumn;
    @FXML
    private TableColumn<FinishedProduct, String> incomeTypeColumn;
    @FXML
    private Button addFinishedProductButton;

    @FXML
    private Button backButton;

    private final DatabaseHandler databaseHandler;
    private final ObservableList<RawMaterial> rawMaterialList = FXCollections.observableArrayList();
    private final ObservableList<FinishedProduct> finishedProductList = FXCollections.observableArrayList();

    public WarehouseController() {
        databaseHandler = new DatabaseHandler();
    }

    @FXML
    public void initialize() {
        // Инициализация таблицы сырья
        rawMaterialIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        tonsPurchasedColumn.setCellValueFactory(new PropertyValueFactory<>("tonsPurchased"));
        purchaseAmountColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseAmount"));
        logisticsCostRawColumn.setCellValueFactory(new PropertyValueFactory<>("logisticsCost"));
        purchaseDateColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        expenseTypeRawColumn.setCellValueFactory(new PropertyValueFactory<>("expenseTypeName"));
        loadRawMaterials();
        rawMaterialsTable.setItems(rawMaterialList);

        // Инициализация таблицы готовой продукции
        finishedProductIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        tonsProducedColumn.setCellValueFactory(new PropertyValueFactory<>("tonsProduced"));
        soldAmountColumn.setCellValueFactory(new PropertyValueFactory<>("soldAmount"));
        logisticsCostFinishedColumn.setCellValueFactory(new PropertyValueFactory<>("logisticsCost"));
        incomeTypeColumn.setCellValueFactory(new PropertyValueFactory<>("incomeTypeName"));
        loadFinishedProducts();
        finishedProductsTable.setItems(finishedProductList);
    }

    private void loadRawMaterials() {
        rawMaterialList.clear();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = databaseHandler.getConnection();
            String query = "SELECT rm.id, rm.tons_purchased, rm.purchase_amount, rm.logistics_cost, rm.purchase_date, et.type_name " +
                    "FROM raw_materials rm " +
                    "JOIN expense_types et ON rm.expense_type_id = et.id";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                rawMaterialList.add(new RawMaterial(
                        resultSet.getInt("id"),
                        resultSet.getDouble("tons_purchased"),
                        resultSet.getDouble("purchase_amount"),
                        resultSet.getDouble("logistics_cost"),
                        LocalDate.parse(resultSet.getString("purchase_date")),
                        0, // expenseTypeId не нужен для отображения
                        resultSet.getString("type_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Ошибка базы данных", "Не удалось загрузить данные о сырье.");
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

    private void loadFinishedProducts() {
        finishedProductList.clear();
        Connection connection = null;
        ResultSet resultSet = null;
        DatabaseHandler localDatabaseHandler = new DatabaseHandler(); // Локальный экземпляр
        try {
            connection = localDatabaseHandler.getConnection(); // Получаем новое соединение
            String query = "SELECT fp.id, fp.tons_produced, fp.sold_amount, fp.logistics_cost, inc_et.type_name AS income_type, exp_et.type_name AS expense_type " +
                    "FROM finished_products fp " +
                    "JOIN expense_types inc_et ON fp.income_type_id = inc_et.id " +
                    "JOIN expense_types exp_et ON fp.expense_type_id = exp_et.id";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    finishedProductList.add(new FinishedProduct(
                            resultSet.getInt("id"),
                            resultSet.getDouble("tons_produced"),
                            resultSet.getDouble("sold_amount"),
                            resultSet.getDouble("logistics_cost"),
                            0, // incomeTypeId не нужен для отображения
                            resultSet.getString("income_type"),
                            0, // expenseTypeId не нужен для отображения
                            resultSet.getString("expense_type")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Ошибка базы данных", "Не удалось загрузить данные о готовой продукции.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            localDatabaseHandler.closeConnection(connection);
            try {
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void handleAddRawMaterial() {
        Dialog<RawMaterial> dialog = new Dialog<>();
        dialog.setTitle("Добавить сырье");
        dialog.setHeaderText("Введите данные о закупке сырья:");

        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField tonsPurchasedField = new TextField();
        tonsPurchasedField.setPromptText("Тонн закуплено");
        TextField purchaseAmountField = new TextField();
        purchaseAmountField.setPromptText("Сумма закупки");
        TextField logisticsCostField = new TextField();
        logisticsCostField.setPromptText("Сумма на логистику");
        DatePicker purchaseDateField = new DatePicker();
        purchaseDateField.setPromptText("Дата покупки");

        grid.add(new Label("Тонн закуплено:"), 0, 0);
        grid.add(tonsPurchasedField, 1, 0);
        grid.add(new Label("Сумма закупки:"), 0, 1);
        grid.add(purchaseAmountField, 1, 1);
        grid.add(new Label("Сумма на логистику:"), 0, 2);
        grid.add(logisticsCostField, 1, 2);
        grid.add(new Label("Дата покупки:"), 0, 3);
        grid.add(purchaseDateField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == addButtonType) {
                try {
                    double tons = Double.parseDouble(tonsPurchasedField.getText());
                    double purchaseAmount = Double.parseDouble(purchaseAmountField.getText());
                    double logistics = Double.parseDouble(logisticsCostField.getText());
                    LocalDate date = purchaseDateField.getValue();
                    if (date != null) {
                        return new RawMaterial(0, tons, purchaseAmount, logistics, date, 0, "Покупка металла");
                    } else {
                        showAlert("Ошибка ввода", "Необходимо выбрать дату покупки.");
                        return null;
                    }
                } catch (NumberFormatException e) {
                    showAlert("Ошибка ввода", "Пожалуйста, введите корректные числовые значения.");
                    return null;
                }
            }
            return null;
        });

        Optional<RawMaterial> result = dialog.showAndWait();

        result.ifPresent(rawMaterial -> {
            addRawMaterialToDatabase(rawMaterial);
            loadRawMaterials();
        });
    }

    private void addRawMaterialToDatabase(RawMaterial rawMaterial) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = databaseHandler.getConnection();
            String query = "INSERT INTO raw_materials (tons_purchased, purchase_amount, logistics_cost, purchase_date, expense_type_id) " +
                    "VALUES (?, ?, ?, ?, (SELECT id FROM expense_types WHERE type_name = 'Покупка металла'))";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, rawMaterial.getTonsPurchased());
            preparedStatement.setDouble(2, rawMaterial.getPurchaseAmount());
            preparedStatement.setDouble(3, rawMaterial.getLogisticsCost());
            preparedStatement.setString(4, rawMaterial.getPurchaseDate().toString());
            preparedStatement.executeUpdate();

            // Автоматическое добавление записей в таблицу доходов и расходов
            addIncomeExpenseRecord("Расход", rawMaterial.getPurchaseDate(), "Покупка металла", rawMaterial.getPurchaseAmount());
            addIncomeExpenseRecord("Расход", rawMaterial.getPurchaseDate(), "Расходы на логистику", rawMaterial.getLogisticsCost());

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Ошибка базы данных", "Не удалось добавить данные о сырье.");
        } finally {
            databaseHandler.closeConnection(connection);
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void handleAddFinishedProduct() {
        Dialog<FinishedProduct> dialog = new Dialog<>();
        dialog.setTitle("Добавить готовую продукцию");
        dialog.setHeaderText("Введите данные о произведенной продукции:");

        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField tonsProducedField = new TextField();
        tonsProducedField.setPromptText("Тонн произведено");
        TextField soldAmountField = new TextField();
        soldAmountField.setPromptText("Продано на сумму");
        TextField logisticsCostField = new TextField();
        logisticsCostField.setPromptText("Затраты на логистику");

        grid.add(new Label("Тонн произведено:"), 0, 0);
        grid.add(tonsProducedField, 1, 0);
        grid.add(new Label("Продано на сумму:"), 0, 1);
        grid.add(soldAmountField, 1, 1);
        grid.add(new Label("Затраты на логистику:"), 0, 2);
        grid.add(logisticsCostField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == addButtonType) {
                try {
                    double tons = Double.parseDouble(tonsProducedField.getText());
                    double sold = Double.parseDouble(soldAmountField.getText());
                    double logistics = Double.parseDouble(logisticsCostField.getText());
                    return new FinishedProduct(0, tons, sold, logistics, 0, "Сбыт арматурных изделий", 0, "Расходы на логистику");
                } catch (NumberFormatException e) {
                    showAlert("Ошибка ввода", "Пожалуйста, введите корректные числовые значения.");
                    return null;
                }
            }
            return null;
        });

        Optional<FinishedProduct> result = dialog.showAndWait();

        result.ifPresent(finishedProduct -> {
            addFinishedProductToDatabase(finishedProduct);
            loadFinishedProducts();
        });
    }

    private void addFinishedProductToDatabase(FinishedProduct finishedProduct) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = databaseHandler.getConnection();
            String query = "INSERT INTO finished_products (tons_produced, sold_amount, logistics_cost, income_type_id, expense_type_id) " +
                    "VALUES (?, ?, ?, (SELECT id FROM expense_types WHERE type_name = 'Сбыт арматурных изделий'), (SELECT id FROM expense_types WHERE type_name = 'Расходы на логистику'))";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, finishedProduct.getTonsProduced());
            preparedStatement.setDouble(2, finishedProduct.getSoldAmount());
            preparedStatement.setDouble(3, finishedProduct.getLogisticsCost());
            preparedStatement.executeUpdate();

            // Автоматическое добавление записей в таблицу доходов и расходов
            addIncomeExpenseRecord("Доход", LocalDate.now(), "Сбыт арматурных изделий", finishedProduct.getSoldAmount());
            addIncomeExpenseRecord("Расход", LocalDate.now(), "Расходы на логистику", finishedProduct.getLogisticsCost());

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Ошибка базы данных", "Не удалось добавить данные о готовой продукции.");
        } finally {
            databaseHandler.closeConnection(connection);
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void addIncomeExpenseRecord(String type, LocalDate date, String expenseTypeName, double amount) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = databaseHandler.getConnection();
            String query = "INSERT INTO income_expenses (type, date, expense_type_id, amount) " +
                    "VALUES (?, ?, (SELECT id FROM expense_types WHERE type_name = ?), ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, date.toString());
            preparedStatement.setString(3, expenseTypeName);
            preparedStatement.setDouble(4, amount);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Ошибка базы данных", "Не удалось добавить запись в доходы/расходы.");
        } finally {
            databaseHandler.closeConnection(connection);
            try {
                if (preparedStatement != null) preparedStatement.close();
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
            showAlert("Ошибка загрузки", "Не удалось вернуться на главный экран.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
