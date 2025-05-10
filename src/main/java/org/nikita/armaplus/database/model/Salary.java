package org.nikita.armaplus.database.model;

import java.time.LocalDate;

public class Salary {
    private int id;
    private int employeeId;
    private String employeeLastName; // Для отображения
    private double amount;
    private LocalDate paymentDate;
    private int expenseTypeId;
    private String expenseTypeName; // Для отображения

    public Salary(int id, int employeeId, String employeeLastName, double amount, LocalDate paymentDate, int expenseTypeId, String expenseTypeName) {
        this.id = id;
        this.employeeId = employeeId;
        this.employeeLastName = employeeLastName;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.expenseTypeId = expenseTypeId;
        this.expenseTypeName = expenseTypeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public int getExpenseTypeId() {
        return expenseTypeId;
    }

    public void setExpenseTypeId(int expenseTypeId) {
        this.expenseTypeId = expenseTypeId;
    }

    public String getExpenseTypeName() {
        return expenseTypeName;
    }

    public void setExpenseTypeName(String expenseTypeName) {
        this.expenseTypeName = expenseTypeName;
    }
}