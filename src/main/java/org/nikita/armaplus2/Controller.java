package org.nikita.armaplus2;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {
    @FXML private ComboBox<String> typeBox;
    @FXML private TextField amountField, dateField, descriptionField;
    @FXML private TableView<FinancialOperation> table;
    @FXML private TableColumn<FinancialOperation, String> colType, colDate, colDesc;
    @FXML private TableColumn<FinancialOperation, Double> colAmount;

    @FXML
    public void initialize() {
        colType.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getType()));
        colAmount.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getAmount()));
        colDate.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDate()));
        colDesc.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));
        refreshTable();
    }

    public void addOperation() {
        try {
            String type = typeBox.getValue();
            double amount = Double.parseDouble(amountField.getText());
            String date = dateField.getText();
            String desc = descriptionField.getText();

            FinancialOperation op = new FinancialOperation(0, type, amount, date, desc);
            FinancialOperationDAO.insert(op);
            refreshTable();

            amountField.clear();
            dateField.clear();
            descriptionField.clear();
        } catch (Exception e) {
            showAlert("Ошибка", "Неверный формат данных");
        }
    }

    private void refreshTable() {
        table.setItems(FXCollections.observableArrayList(FinancialOperationDAO.getAll()));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
