package com.seguro.model;

public class Transaction {

    private final String id;
    private final TxType type;
    private final Money money;
    private final String description;

    public Transaction(String id, TxType type, Money money, String description) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id no puede estar vacío");
        }
        if (type == null) {
            throw new IllegalArgumentException("El tipo no puede ser nulo");
        }
        if (money == null) {
            throw new IllegalArgumentException("El monto no puede ser nulo");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía");
        }

        this.id = id.trim();
        this.type = type;
        this.money = money;
        this.description = description.trim();
    }

    public String getId() {
        return id;
    }

    public TxType getType() {
        return type;
    }

    public Money getMoney() {
        return money;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Transaction{id='" + id + "', type=" + type
                + ", money=" + money + ", description='" + description + "'}";
    }
}