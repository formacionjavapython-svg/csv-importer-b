package main.java;

public class Transaction extends Money {
    public enum Type {
        IN, OUT
    }

    private final String id;
    private final Type type;
    private final Money cantidad;
    private final String descripcion;

    // Constructor privado para forzar el uso del factory method

    private Transaction(String id, Type type, Money cantidad, String descripcion) {
        this.id = id;
        this.type = type;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
    }

    public static Transaction of(String id, String type, String cantidad, String moneda, String descripcion) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Se requiere un id");
        }

        Type txType;
        try {
            txType = Type.valueOf(type.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Type inválido: " + type);
        }

        Money money = new Money(cantidad, moneda);

        return new Transaction(id, txType, money, descripcion);
    }

    public Type getType() {
        return type;
    }

    public Money getCantidad() {
        return cantidad;
    }
}
