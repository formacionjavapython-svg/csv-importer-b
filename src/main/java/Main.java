package main.java;

public class Main {
    public static void main(String[] args) {
        // Crear una instancia de Money
        Money money = new Money("100.00", "MXN");

        // Imprimir los detalles de la transacción
        System.out.println("Monto: " + money.getCantidad());
        System.out.println("Moneda: " + money.getMoneda());
        System.out.println("Tipo de Transacción: " + money.getType());

    }
}