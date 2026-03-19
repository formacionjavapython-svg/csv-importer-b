package main.java;

import java.math.BigDecimal;
import java.util.Objects;

// Atributos de la clase Money
// Encapsulamiento
public class Money {

    private final BigDecimal cantidad;
    private final String moneda;

    public Money(String cantidad, String moneda) {
        if (moneda == null || moneda.isBlank()) {
            throw new IllegalArgumentException("moneda is required");
        }

        try {
            this.cantidad = new BigDecimal(cantidad);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid cantidad: " + cantidad);
        }

        if (this.cantidad.scale() > 2) {
            throw new IllegalArgumentException("Max 2 decimal places allowed");
        }

        this.moneda = moneda.toUpperCase();
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public String getMoneda() {
        return moneda;
    }

    public String getType() {
        return "MXN".equals(moneda) ? "Pesos" : "Dólares";
    }

    public Money add(Money other) {
        validatemoneda(other);
        return new Money(this.cantidad.add(other.cantidad).toString(), moneda);
    }

    private void validatemoneda(Money other) {
        if (!this.moneda.equals(other.moneda)) {
            throw new IllegalArgumentException("no esxiste el tipo de moneda");
        }
    }

    @Override
    public String toString() {
        return cantidad + " " + moneda;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Money))
            return false;
        Money m = (Money) o;
        return cantidad.equals(m.cantidad) && moneda.equals(m.moneda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cantidad, moneda);
    }
}