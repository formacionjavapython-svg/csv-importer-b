package com.axity.csvimporter.model;

import java.util.Objects;

public class Transaction {
    private final String id;
    private final TransactionType type;
    private final Money money;
    private final String description;

    private Transaction(String id, TransactionType type, Money money, String description) {
        this.id = id;
        this.type = type;
        this.money = money;
        this.description = description;
    }

    public static Transaction of(String id, String typeStr, String amount, String currency, String description) {
        // Validaciones
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        Objects.requireNonNull(typeStr, "El tipo no puede ser nulo");
        Objects.requireNonNull(amount, "El monto no puede ser nulo");
        Objects.requireNonNull(currency, "La moneda no puede ser nula");

        if (id.isBlank()) {
            throw new IllegalArgumentException("El ID no puede estar vacío");
        }

        // Validar y convertir el tipo
        TransactionType type;
        try {
            type = TransactionType.valueOf(typeStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de transacción inválido: " + typeStr + ". Debe ser IN o OUT");
        }

        // Crear el objeto Money (ya valida formato y positividad)
        Money money = Money.of(amount, currency);

        // Descripción puede ser vacía, pero no nula
        String safeDescription = description != null ? description.trim() : "";

        return new Transaction(id.trim(), type, money, safeDescription);
    }

    public String getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public Money getMoney() {
        return money;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id) &&
                type == that.type &&
                Objects.equals(money, that.money) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, money, description);
    }

    @Override
    public String toString() {
        return String.format("Transaction{id='%s', type=%s, money=%s, description='%s'}",
                id, type, money, description);
    }
}