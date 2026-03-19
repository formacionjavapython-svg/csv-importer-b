package main;

import java.util.ArrayList;
import java.util.List;

public class ImportResult {
    private int validCount;
    private int invalidCount;
    private double totalIn;
    private double totalOut;
    private List<String> errors;

    public ImportResult() {
        this.validCount = 0;
        this.invalidCount = 0;
        this.totalIn = 0;
        this.totalOut = 0;
        this.errors = new ArrayList<>();
    }

    public void addValidTransaction(Transaction transaction) {
        validCount++;
        if (transaction.getType().equals("IN")) {
            totalIn += transaction.getMoney().getAmount();
        } else {
            totalOut += transaction.getMoney().getAmount();
        }
    }

    public void addInvalidTransaction(String error) {
        invalidCount++;
        errors.add(error);
    }

    public int getValidCount() {
        return validCount;
    }

    public int getInvalidCount() {
        return invalidCount;
    }

    public double getTotalIn() {
        return totalIn;
    }

    public double getTotalOut() {
        return totalOut;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void printSummary() {
        System.out.println("Transacciones válidas: " + validCount);
        System.out.println("Transacciones inválidas: " + invalidCount);
        System.out.println("Total IN: " + totalIn);
        System.out.println("Total OUT: " + totalOut);
        if (!errors.isEmpty()) {
            System.out.println("Errores encontrados: " + errors);
        }
    }
}
