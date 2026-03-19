package main.java.com.seguro.model;

public class Transaction {
    private String id;
    private String date;
    private Money amount;
    private String description;

    public Transaction(String id, String date, Money amount, String description) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.description = description;
    }

    // Getters
    public String getId() { return id; }
    public String getDate() { return date; }
    public Money getAmount() { return amount; }
    public String getDescription() { return description; }
}