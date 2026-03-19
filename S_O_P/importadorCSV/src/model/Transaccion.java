package model;

import java.util.Objects;

public class Transaccion {
    private final String id;
    private final String tipo; // "in" o "out"
    private final Money monto;
    private final String descripcion;

    // Constructor privado para forzar uso de factory method
    private Transaccion(String id, String tipo, Money monto, String descripcion) {
        // Validar ID
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID no puede ser nulo o vacío");
        }

        // Validar tipo
        if (tipo == null || (!tipo.equalsIgnoreCase("in") && !tipo.equalsIgnoreCase("out"))) {
            throw new IllegalArgumentException("El tipo debe ser 'in' o 'out': " + tipo);
        }

        // Validar monto
        if (monto == null) {
            throw new IllegalArgumentException("El monto no puede ser nulo");
        }

        // Validar descripción
        if (descripcion == null) {
            throw new IllegalArgumentException("La descripción no puede ser nula");
        }

        this.id = id.trim();
        this.tipo = tipo.toLowerCase();
        this.monto = monto;
        this.descripcion = descripcion;
    }

    // Factory method principal
    public static Transaccion of(String id, String tipo, Money monto, String descripcion) {
        return new Transaccion(id, tipo, monto, descripcion);
    }

    // Factory method alternativo
    public static Transaccion of(String id, String tipo, String moneda, String cantidadStr, String descripcion) {
        Money monto = Money.of(cantidadStr, moneda);
        return new Transaccion(id, tipo, monto, descripcion);
    }

    // Métodos de negocio
    public boolean esIngreso() {
        return "in".equals(tipo);
    }

    public boolean esEgreso() {
        return "out".equals(tipo);
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public Money getMonto() {
        return monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaccion that = (Transaccion) o;
        return id.equals(that.id) &&
                tipo.equals(that.tipo) &&
                monto.equals(that.monto) &&
                descripcion.equals(that.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipo, monto, descripcion);
    }

    @Override
    public String toString() {
        return String.format("Transaccion{id='%s', tipo='%s', monto=%s, desc='%s'}",
                id, tipo, monto, descripcion);
    }
}
