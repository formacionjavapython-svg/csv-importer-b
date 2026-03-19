package main;

public class Transaction {
    private String id;
    private String type; 
    private Money money;
    private String description;

    public Transaction(String id, String type, Money money, String description) {
        validateTransaction(id, type, money);
        this.id = id;
        this.type = type;
        this.money = money;
        this.description = description;
    }

    private void validateTransaction(String id, String type, Money money) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID no puede ser nulo o vacío.");
        }
        if (!type.equals("IN") && !type.equals("OUT")) {
            throw new IllegalArgumentException("Tipo debe ser 'IN' o 'OUT'.");
        }
        if (money == null) {
            throw new IllegalArgumentException("Monto no puede ser nulo.");
        }
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Money getMoney() {
        return money;
    }

    public String getDescription() {
        return description;
    }
}
