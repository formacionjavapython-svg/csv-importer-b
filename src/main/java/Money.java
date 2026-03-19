package main.java;

import java.math.BigDecimal;

public final class Money {
    private final BigDecimal amount;
    private final String currency;

    public Money(BigDecimal amount, String currency) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        this.amount = amount;
        this.currency = currency.trim().toUpperCase();
    }

    public static Money parse(String amountStr, String currency) {
        if (amountStr == null || amountStr.isBlank()) {
            return null;
        }
        try {
            BigDecimal amount = new BigDecimal(amountStr.trim());
            return new Money(amount, currency);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add different currencies");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    @Override
    public String toString() {
        return amount.toPlainString() + " " + currency;
    }
}