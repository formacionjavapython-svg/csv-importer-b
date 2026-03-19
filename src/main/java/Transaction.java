package main.java;

public class Transaction {
    public enum TxType { IN, OUT }

    private final String id;
    private final TxType type;
    private final Money money;
    private final String description;

    public Transaction(String id, TxType type, Money money, String description) {
        this.id = id;
        this.type = type;
        this.money = money;
        this.description = description;
    }
    
    public TxType getType() { return type; }
    public Money getMoney() { return money; }
    public String getId() { return id; }
    public String getDescription() { return description; }
}