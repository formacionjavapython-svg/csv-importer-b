package com.escuela.csvimporter.model;

public class Transaction {
    private final String id;
    private final TxType type;
    private final Money money;
    private final String description;

    public Transaction(String id, TxType type, Money money, String description) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id es obligatorio");
        }

        if (type == null) {
            throw new IllegalArgumentException("El tipo es obligatorio");
        }

        if (money == null) {
            throw new IllegalArgumentException("El monto es obligatorio");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }

        this.id = id;
        this.type = type;
        this.money = money;
        this.description = description;
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
}