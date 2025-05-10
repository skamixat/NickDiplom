package org.nikita.armaplus.controllers;

import org.nikita.armaplus.MainApplication;
import org.nikita.armaplus.database.DatabaseHandler;
import org.nikita.armaplus.utils.PasswordManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private Button adminButton;

    @FXML
    private Button accountantButton;

    @FXML
    private Button warehouseButton;

    private final DatabaseHandler databaseHandler;
    private final PasswordManager passwordManager;

    public LoginController() {
        databaseHandler = new DatabaseHandler();
        passwordManager = new PasswordManager(databaseHandler);
    }

    @FXML
    protected void handleAdminLogin() {
        showPasswordDialog("Начальник", "admin-view.fxml");
    }

    @FXML
    protected void handleAccountantLogin() {
        showPasswordDialog("Бухгалтер", "accountant-view.fxml");
    }

    @FXML
    protected void handleWarehouseLogin() {
        showPasswordDialog("Кладовщик", "warehouse-view.fxml");
    }

    private void showPasswordDialog(String role, String viewPath) {
        Stage passwordDialog = new Stage();
        passwordDialog.initModality(Modality.APPLICATION_MODAL);
        passwordDialog.setTitle("Введите пароль для " + role);

        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Войти");
        Button cancelButton = new Button("Отмена");

        VBox layout = new VBox(10);
        layout.getChildren().addAll(passwordField, loginButton, cancelButton);
        layout.setAlignment(javafx.geometry.Pos.CENTER);
        layout.setPadding(new javafx.geometry.Insets(15));

        Scene scene = new Scene(layout, 250, 150);
        passwordDialog.setScene(scene);
        passwordDialog.show();

        loginButton.setOnAction(event -> {
            String enteredPassword = passwordField.getText();
            try {
                if (passwordManager.verifyPassword(role, enteredPassword)) {
                    passwordDialog.close();
                    loadMainView(viewPath);
                } else {
                    showAlert("Ошибка", "Неверный пароль для роли " + role);
                }
            } catch (SQLException e) {
                showAlert("Ошибка базы данных", "Не удалось проверить пароль: " + e.getMessage());
            }
        });

        cancelButton.setOnAction(event -> passwordDialog.close());
    }

    private void loadMainView(String viewPath) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(viewPath));
            Stage stage = (Stage) adminButton.getScene().getWindow(); // Получаем текущее окно
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка загрузки", "Не удалось загрузить视图: " + e.getMessage());
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