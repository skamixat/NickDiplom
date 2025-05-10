package org.nikita.armaplus2.database.model;


import java.time.LocalDate;

public class FinishedProduct {
    private int id;
    private double tonsProduced;
    private double soldAmount;
    private double logisticsCost;
    private int incomeTypeId;
    private String incomeTypeName; // Для отображения
    private int expenseTypeId;
    private String expenseTypeName; // Для отображения

    public FinishedProduct(int id, double tonsProduced, double soldAmount, double logisticsCost, int incomeTypeId, String incomeTypeName, int expenseTypeId, String expenseTypeName) {
        this.id = id;
        this.tonsProduced = tonsProduced;
        this.soldAmount = soldAmount;
        this.logisticsCost = logisticsCost;
        this.incomeTypeId = incomeTypeId;
        this.incomeTypeName = incomeTypeName;
        this.expenseTypeId = expenseTypeId;
        this.expenseTypeName = expenseTypeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTonsProduced() {
        return tonsProduced;
    }

    public void setTonsProduced(double tonsProduced) {
        this.tonsProduced = tonsProduced;
    }

    public double getSoldAmount() {
        return soldAmount;
    }

    public void setSoldAmount(double soldAmount) {
        this.soldAmount = soldAmount;
    }

    public double getLogisticsCost() {
        return logisticsCost;
    }

    public void setLogisticsCost(double logisticsCost) {
        this.logisticsCost = logisticsCost;
    }

    public int getIncomeTypeId() {
        return incomeTypeId;
    }

    public void setIncomeTypeId(int incomeTypeId) {
        this.incomeTypeId = incomeTypeId;
    }

    public String getIncomeTypeName() {
        return incomeTypeName;
    }

    public void setIncomeTypeName(String incomeTypeName) {
        this.incomeTypeName = incomeTypeName;
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