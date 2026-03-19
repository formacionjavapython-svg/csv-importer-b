package com.seguro.model;

import java.math.BigDecimal;

public class Money {

    private final BigDecimal amount;
    private final String currency;

    public Money(String amount, String currency) {
        if (amount == null || amount.isBlank()) {
            throw new IllegalArgumentException("El monto no puede estar vacío");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("La moneda no puede estar vacía");
        }

        this.amount = new BigDecimal(amount.trim());

        if (this.amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }

        this.currency = currency.trim().toUpperCase();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}