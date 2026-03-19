package main.java;

public class Transaction {
    private String id;
    private TxType tipo;
    private String moneda;
    private double monto;

    public Transaction(String id, TxType tipo, String moneda, double monto) {
        this.id = id;
        this.tipo = tipo;
        this.moneda = moneda;
        this.monto = monto;
    }
    public String getId() {
        return id;
    }
    public TxType getTipo() {
        return tipo;
    }
    public String getMoneda() {
        return moneda;
    }
    public double getMonto() {
        return monto;
    }

    public void mostrarInfo() {
        System.out.println("ID: " + id);
        System.out.println("Tipo: " + tipo);
        System.out.println("Moneda: " + moneda);
        System.out.println("Monto: " + monto);
        System.out.println("----------------------------");
    }
}
