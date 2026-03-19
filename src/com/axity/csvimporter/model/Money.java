package com.axity.csvimporter.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {
    private final BigDecimal amount;
    private final String currency;

    private Money(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public static Money of(String amountStr, String currency) {
        // Validaciones básicas
        Objects.requireNonNull(amountStr, "El monto no puede ser nulo");
        Objects.requireNonNull(currency, "La moneda no puede ser nula");

        if (amountStr.isBlank()) {
            throw new IllegalArgumentException("El monto no puede estar vacío");
        }

        if (currency.isBlank()) {
            throw new IllegalArgumentException("La moneda no puede estar vacía");
        }

        try {
            // Reemplazar coma por punto SOLO si es el separador decimal europeo
            // y no hay otro punto en el string
            String normalizedAmount = amountStr;
            if (amountStr.contains(",") && !amountStr.contains(".")) {
                normalizedAmount = amountStr.replace(",", ".");
            }

            BigDecimal amount = new BigDecimal(normalizedAmount);

            // Validar que el monto sea positivo
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("El monto debe ser positivo");
            }

            return new Money(amount, currency.toUpperCase());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato de monto inválido: " + amountStr);
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
            throw new IllegalArgumentException("No se pueden sumar monedas diferentes: "
                    + this.currency + " vs " + other.currency);
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0 &&
                Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", amount, currency);
    }
}