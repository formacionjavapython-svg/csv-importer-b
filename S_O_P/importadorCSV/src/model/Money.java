package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {
    private final BigDecimal cantidad;
    private final String moneda;

    private Money(BigDecimal cantidad, String moneda) {
        // Validar que cantidad sea positiva
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        this.cantidad = cantidad.setScale(2, RoundingMode.HALF_UP);
        this.moneda = Objects.requireNonNull(moneda, "La moneda no puede ser nula").toUpperCase();
    }

    // Factory method principal
    public static Money of(String cantidadStr, String moneda) {
        try {
            BigDecimal cantidad = new BigDecimal(cantidadStr);
            return new Money(cantidad, moneda);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor monetario inválido: " + cantidadStr);
        }
    }

    // Factory method alternativo
    public static Money of(BigDecimal cantidad, String moneda) {
        return new Money(cantidad, moneda);
    }

    // Operación segura: sumar dos montos de la misma moneda
    public Money sumar(Money otro) {
        if (!this.moneda.equals(otro.moneda)) {
            throw new IllegalArgumentException(
                    String.format("No se pueden sumar monedas diferentes: %s y %s",
                            this.moneda, otro.moneda));
        }
        return new Money(this.cantidad.add(otro.cantidad), this.moneda);
    }

    // Getters
    public BigDecimal getCantidad() {
        return cantidad;
    }

    public String getMoneda() {
        return moneda;
    }

    // Métodos de utilidad
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return cantidad.equals(money.cantidad) && moneda.equals(money.moneda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cantidad, moneda);
    }

    @Override
    public String toString() {
        return String.format("%s %.2f", moneda, cantidad);
    }
}