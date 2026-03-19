package main.java;

import java.util.Objects;

public final class Transaction {
    private final String id;
    private final TxType type;
    private final Money money;
    private final String description;

    public Transaction(String id, TxType type, Money money, String description) {
        this.id = validateId(id);
        this.type = validateType(type);
        this.money = validateMoney(money);
        this.description = description != null ? description.trim() : "";
    }

    private String validateId(String id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        String trimmed = id.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("El ID no puede estar vacío");
        }
        return trimmed;
    }

    private TxType validateType(TxType type) {
        Objects.requireNonNull(type, "El tipo de transacción no puede ser nulo");
        return type;
    }

    private Money validateMoney(Money money) {
        Objects.requireNonNull(money, "El monto no puede ser nulo");
        return money;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id.equals(that.id) && type == that.type && money.equals(that.money) && description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, money, description);
    }

    @Override
    public String toString() {
        return String.format("Transaction{id='%s', type=%s, money=%s, description='%s'}", id, type, money, description);
    }
}