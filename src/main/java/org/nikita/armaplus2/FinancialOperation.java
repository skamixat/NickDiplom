package org.nikita.armaplus2;

public class FinancialOperation {
    private int id;
    private String type;
    private double amount;
    private String date;
    private String description;

    public FinancialOperation(int id, String type, double amount, String date, String description) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public String getDescription() { return description; }
}
