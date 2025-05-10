package org.nikita.armaplus.database.model;

import java.time.LocalDate;

public class RawMaterial {
    private int id;
    private double tonsPurchased;
    private double purchaseAmount;
    private double logisticsCost;
    private LocalDate purchaseDate;
    private int expenseTypeId;
    private String expenseTypeName; // Для отображения

    public RawMaterial(int id, double tonsPurchased, double purchaseAmount, double logisticsCost, LocalDate purchaseDate, int expenseTypeId, String expenseTypeName) {
        this.id = id;
        this.tonsPurchased = tonsPurchased;
        this.purchaseAmount = purchaseAmount;
        this.logisticsCost = logisticsCost;
        this.purchaseDate = purchaseDate;
        this.expenseTypeId = expenseTypeId;
        this.expenseTypeName = expenseTypeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTonsPurchased() {
        return tonsPurchased;
    }

    public void setTonsPurchased(double tonsPurchased) {
        this.tonsPurchased = tonsPurchased;
    }

    public double getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(double purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public double getLogisticsCost() {
        return logisticsCost;
    }

    public void setLogisticsCost(double logisticsCost) {
        this.logisticsCost = logisticsCost;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
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