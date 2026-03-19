package main.java;

import java.util.ArrayList;

public class ImportResult {
    private int conteoValido;
    private int conteoInvalido;
    private double totalIngresos;
    private double totalEgresos;
    private ArrayList<String> errorsList;
    private ArrayList<Transaction> validTransactions;

    public int getConteoValido() {
        return conteoValido;
    }
    public int getConteoInvalido() {
        return conteoInvalido;
    }
    public double getTotalIngresos() {
        return totalIngresos;
    }
    public double getTotalEgresos() {
        return totalEgresos;
    }

    public ImportResult() {
        this.conteoValido = 0;
        this.conteoInvalido = 0;
        this.totalIngresos = 0;
        this.totalEgresos = 0;
        this.errorsList = new ArrayList<>();
        this.validTransactions = new ArrayList<>();
    }

    public void agregaTrasacionValida(Transaction trasacion) {
        conteoValido++;
        validTransactions.add(trasacion);

        if (trasacion.getTipo()==TxType.IN){
            totalIngresos += trasacion.getMonto();
        }else {
            totalEgresos += trasacion.getMonto();
        }
    }
    public void agregaError(String errorMessage) {
        conteoInvalido++;
        errorsList.add(errorMessage);
    }

    public void resumen() {
        System.out.println("\n========== RESUMEN ==========");
        System.out.println("Validas: " + conteoValido);
        System.out.println("Invalidas: " + conteoInvalido);
        System.out.println("Total Ingresos: $" + String.format("%.2f", totalIngresos));
        System.out.println("Total Egresos: $" + String.format("%.2f", totalEgresos));
        System.out.println("Balance: $" + String.format("%.2f", (totalIngresos - totalEgresos)));

        if (errorsList.size() > 0) {
            System.out.println("===== ERRORES =====");
            for (String error : errorsList) {
                System.out.println("- " + error);
            }
        }
    }
}
