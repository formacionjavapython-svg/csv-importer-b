package com.csvimporter;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value object que encapsula un monto monetario y su moneda.
 *
 * <p>Esta clase es inmutable y válida sus argumentos en tiempo de construcción.
 * Usar {@link #Money(String, String)} para crear instancias a partir de cadenas
 * o {@link #Money(BigDecimal, String)} para valores numéricos.</p>
 */
public final class Money {
    private final BigDecimal amount;
    private final String currency;

    /**
     * Crea una instancia de {@code Money} a partir de una cadena con el monto.
     *
     * @param amountStr  cadena representando el monto. Debe ser un número válido
     * @param currency   código de la moneda (no puede ser nulo ni vacío)
     * @throws IllegalArgumentException si el monto o la moneda son inválidos
     */
    public Money(String amountStr, String currency) {
        this(parseAmount(amountStr), currency);
    }

    /**
     * Crea una instancia de {@code Money} a partir de un {@code BigDecimal}.
     *
     * @param amount   monto en {@code BigDecimal}, no puede ser nulo ni negativo
     * @param currency código de la moneda, no puede ser nulo ni vacío
     * @throws IllegalArgumentException si el monto o la moneda son inválidos
     */
    public Money(BigDecimal amount, String currency) {
        Objects.requireNonNull(amount, "El monto no puede ser nulo");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        Objects.requireNonNull(currency, "La moneda no puede ser nula");
        if (currency.isBlank()) {
            throw new IllegalArgumentException("La moneda no puede estar vacía");
        }
        this.amount = amount;
        this.currency = currency;
    }

    private static BigDecimal parseAmount(String amountStr) {
        Objects.requireNonNull(amountStr, "El monto no puede ser nulo");
        if (amountStr.isBlank()) {
            throw new IllegalArgumentException("El monto no puede estar vacío");
        }
        try {
            return new BigDecimal(amountStr);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Formato de monto inválido: " + amountStr, ex);
        }
    }

    /**
     * Obtiene el monto monetario.
     *
     * @return monto como {@code BigDecimal}
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Obtiene el código de la moneda.
     *
     * @return cadena con la moneda
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Devuelve una nueva instancia con la suma de este monto y el monto indicado.
     *
     * @param other otro valor monetario con la misma moneda
     * @return nuevo {@code Money} con la suma de ambos
     * @throws IllegalArgumentException si las monedas no coinciden
     */
    public Money add(Money other) {
        Objects.requireNonNull(other, "No se puede sumar con un valor nulo");
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Las monedas deben coincidir para sumar");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    @Override
    public String toString() {
        return amount.toPlainString() + " " + currency;
    }
}