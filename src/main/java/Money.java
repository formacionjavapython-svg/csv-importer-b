package main.java;

public class Money {
    private double monto;
    private String moneda;

    public double getMonto() {
        return monto;
    }
    public String getMoneda() {
        return moneda;
    }

    public Money(double monto, String moneda) {
        if (monto < 0) {
            System.out.println("El monto no puede ser negativo");
            this.monto = 0;
        }else {
            this.monto = monto;
        }

        if (moneda == null || moneda.isEmpty()) {
            System.out.println("La moneda no puede ser nulo o vacio");
            this.moneda = "MXN";
        }else {
            this.moneda = moneda.toUpperCase();
        }
    }
}
