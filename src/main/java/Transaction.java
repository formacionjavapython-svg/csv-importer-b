package main.java;


public class Transaction {

    private final String id;
    private final TxType type;
    private final Money amount;
    private final String description;

    public Transaction(String id, TxType type, Money amount, String description) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }

        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }

        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }

        this.id = id;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public TxType getType() {
        return type;
    }

    public Money getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return id + " | " + type + " | " + amount + " | " + description;
    }
}
