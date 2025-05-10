package org.nikita.armaplus.database.model;


import java.time.LocalDate;

public class IncomeExpense {
    private int id;
    private String type;
    private LocalDate date;
    private int expenseTypeId;
    private String expenseTypeName;
    private double amount;

    public IncomeExpense(int id, String type, LocalDate date, int expenseTypeId, String expenseTypeName, double amount) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.expenseTypeId = expenseTypeId;
        this.expenseTypeName = expenseTypeName;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}