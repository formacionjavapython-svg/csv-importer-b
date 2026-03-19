package main.java;

public class Transaction {

    private String id;
    private TxType tipo;
    private Money money;
    private String descripcion;

    public Transaction(String id, String tipo, String monto, String moneda, String descripcion) {

        if (id == null || id.isBlank())
            throw new IllegalArgumentException("ID inválido");

        this.id = id;
        this.tipo = TxType.valueOf(tipo);
        this.money = new Money(moneda, monto);
        this.descripcion = descripcion;
    }

    public TxType getTipo() {
        return this.tipo;
    }

    public Money getMoney() {
        return this.money;
    }
}