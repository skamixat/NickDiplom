package org.nikita.armaplus2.controllers;

import org.nikita.armaplus2.MainApplication;
import org.nikita.armaplus2.database.DatabaseHandler;
import org.nikita.armaplus2.database.model.Employee;
import org.nikita.armaplus2.database.model.Salary;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class AccountantController {

    @FXML
    private TableView<Employee> employeesTable;
    @FXML
    private TableColumn<Employee, Integer> employeeIdColumn;
    @FXML
    private TableColumn<Employee, String> firstNameColumn;
    @FXML
    private TableColumn<Employee, String> lastNameColumn;
    @FXML
    private TableColumn<Employee, String> phoneColumn;
    @FXML
    private TableColumn<Employee, String> positionColumn;
    @FXML
    private TableColumn<Employee, LocalDate> hireDateColumn;
    @FXML
    private Button addEmployeeButton;
    @FXML
    private Button deleteEmployeeButton;

    @FXML
    private TableView<Salary> salariesTable;
    @FXML
    private TableColumn<Salary, Integer> salaryIdColumn;
    @FXML
    private TableColumn<Salary, String> employeeLastNameSalaryColumn;
    @FXML
    private TableColumn<Salary, Double> amountSalaryColumn;
    @FXML
    private TableColumn<Salary, LocalDate> paymentDateColumn;
    @FXML
    private TableColumn<Salary, String> expenseTypeSalaryColumn;
    @FXML
    private Button addSalaryButton;

    @FXML
    private Button backButton;

    private final DatabaseHandler databaseHandler;
    private final ObservableList<Employee> employeeList = FXCollections.observableArrayList();
    private final ObservableList<Salary> salaryList = FXCollections.observableArrayList();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public AccountantController() {
        databaseHandler = new DatabaseHandler();
    }

    @FXML
    public void initialize() {
        // Инициализация таблицы сотрудников
        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        hireDateColumn.setCellValueFactory(new PropertyValueFactory<>("hireDate"));
        loadEmployees();
        employeesTable.setItems(employeeList);

        // Инициализация таблицы зарплат
        salaryIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        employeeLastNameSalaryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployeeLastName()));
        amountSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        expenseTypeSalaryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExpenseTypeName()));
        loadSalaries();
        salariesTable.setItems(salaryList);
    }

    private void loadEmployees() {
        employeeList.clear();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = databaseHandler.getConnection();
            String query = "SELECT * FROM employees";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                employeeList.add(new Employee(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("position"),
                        LocalDate.parse(resultSet.getString("hire_date"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

    private void loadSalaries() {
        salaryList.clear();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = databaseHandler.getConnection();
            String query = "SELECT s.id, e.last_name, s.amount, s.payment_date, et.type_name " +
                    "FROM salaries s " +
                    "JOIN employees e ON s.employee_id = e.id " +
                    "JOIN expense_types et ON s.expense_type_id = et.id";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery(query);
            while (resultSet.next()) {
                salaryList.add(new Salary(
                        resultSet.getInt("id"),
                        0, // employeeId пока не нужен для отображения
                        resultSet.getString("last_name"),
                        resultSet.getDouble("amount"),
                        LocalDate.parse(resultSet.getString("payment_date")),
                        0, // expenseTypeId пока не нужен для отображения
                        resultSet.getString("type_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            databaseHandler.closeConnection(connection);
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void handleAddEmployee() {
        Dialog<Employee> dialog = new Dialog<>();
        dialog.setTitle("Добавить сотрудника");
        dialog.setHeaderText("Введите данные нового сотрудника:");

        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Имя");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Фамилия");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Номер телефона");
        TextField positionField = new TextField();
        positionField.setPromptText("Должность");
        DatePicker hireDateField = new DatePicker();
        hireDateField.setPromptText("Дата найма");

        grid.add(new Label("Имя:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Фамилия:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Номер телефона:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("Должность:"), 0, 3);
        grid.add(positionField, 1, 3);
        grid.add(new Label("Дата найма:"), 0, 4);
        grid.add(hireDateField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == addButtonType) {
                return new Employee(
                        0, // ID будет присвоен базой данных
                        firstNameField.getText(),
                        lastNameField.getText(),
                        phoneField.getText(),
                        positionField.getText(),
                        hireDateField.getValue()
                );
            }
            return null;
        });

        Optional<Employee> result = dialog.showAndWait();

        result.ifPresent(employee -> {
            if (employee.getHireDate() != null) {
                addEmployeeToDatabase(employee);
                loadEmployees();
            } else {
                showAlert("Ошибка", "Необходимо выбрать дату найма.");
            }
        });
    }

    private void addEmployeeToDatabase(Employee employee) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = databaseHandler.getConnection();
            String query = "INSERT INTO employees (first_name, last_name, phone_number, position, hire_date) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setString(3, employee.getPhoneNumber());
            preparedStatement.setString(4, employee.getPosition());
            preparedStatement.setString(5, employee.getHireDate().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Ошибка базы данных", "Не удалось добавить сотрудника.");
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
    protected void handleDeleteEmployee() {
        Employee selectedEmployee = employeesTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert("Предупреждение", "Выберите сотрудника для удаления.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение удаления");
        alert.setHeaderText("Вы уверены, что хотите удалить сотрудника " + selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteEmployeeFromDatabase(selectedEmployee.getId());
            loadEmployees();
        }
    }

    private void deleteEmployeeFromDatabase(int employeeId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = databaseHandler.getConnection();
            String query = "DELETE FROM employees WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, employeeId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Ошибка базы данных", "Не удалось удалить сотрудника.");
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
    protected void handleAddSalary() {
        Dialog<Salary> dialog = new Dialog<>();
        dialog.setTitle("Выдать зарплату");
        dialog.setHeaderText("Введите данные о выплате зарплаты:");

        ButtonType addButtonType = new ButtonType("Выдать", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        ComboBox<Employee> employeeComboBox = new ComboBox<>();
        loadEmployeesIntoComboBox(employeeComboBox);
        TextField amountField = new TextField();
        amountField.setPromptText("Сумма");
        DatePicker paymentDateField = new DatePicker();
        paymentDateField.setPromptText("Дата выплаты");

        grid.add(new Label("Сотрудник:"), 0, 0);
        grid.add(employeeComboBox, 1, 0);
        grid.add(new Label("Сумма:"), 0, 1);
        grid.add(amountField, 1, 1);
        grid.add(new Label("Дата выплаты:"), 0, 2);
        grid.add(paymentDateField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == addButtonType) {
                Employee selectedEmployee = employeeComboBox.getValue();
                LocalDate paymentDate = paymentDateField.getValue();
                String amountText = amountField.getText();
                if (selectedEmployee != null && paymentDate != null && !amountText.isEmpty()) {
                    try {
                        double amount = Double.parseDouble(amountText);
                        return new Salary(0, selectedEmployee.getId(), selectedEmployee.getLastName(), amount, paymentDate, 0, "Зарплата сотрудников"); // expenseTypeId будет получен из базы
                    } catch (NumberFormatException e) {
                        showAlert("Ошибка ввода", "Сумма должна быть числом.");
                        return null;
                    }
                } else {
                    showAlert("Ошибка ввода", "Пожалуйста, заполните все поля.");
                    return null;
                }
            }
            return null;
        });

        Optional<Salary> result = dialog.showAndWait();

        result.ifPresent(salary -> {
            if (salary.getPaymentDate() != null) {
                addSalaryToDatabase(salary);
                loadSalaries();
            }
        });
    }

    private void loadEmployeesIntoComboBox(ComboBox<Employee> comboBox) {
        comboBox.getItems().clear();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = databaseHandler.getConnection();
            String query = "SELECT id, first_name, last_name FROM employees";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                comboBox.getItems().add(new Employee(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        null, null, null // Остальные поля не нужны для ComboBox
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

    private void addSalaryToDatabase(Salary salary) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = databaseHandler.getConnection();
            String query = "INSERT INTO salaries (employee_id, amount, payment_date, expense_type_id) VALUES (?, ?, ?, (SELECT id FROM expense_types WHERE type_name = 'Зарплата сотрудников'))";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, salary.getEmployeeId());
            preparedStatement.setDouble(2, salary.getAmount());
            preparedStatement.setString(3, salary.getPaymentDate().toString());
            preparedStatement.executeUpdate();

            // Автоматическое добавление записи в таблицу доходов и расходов
            addIncomeExpenseRecord("Расход", salary.getPaymentDate(), "Зарплата сотрудников", salary.getAmount());

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Ошибка базы данных", "Не удалось выдать зарплату.");
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