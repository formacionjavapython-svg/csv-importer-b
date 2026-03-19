package main.java;

import java.util.ArrayList;
import java.util.List;

public class ImportResult extends Money {

    private int validCount;
    private int invalidCount;
    private Money totalIn = new Money("0.00", "MXN");
    private Money totalOut = new Money("0.00", "MXN");
    private final List<String> errors = new ArrayList<>();

    public ImportResult(String cantidad, String moneda) {
        super(cantidad, moneda);
    }

    public void addValid(Transaction tx) {
        validCount++;

        if (tx.getType() == Transaction.Type.IN) {
            totalIn = totalIn.add(tx.getCantidad());
        } else {
            totalOut = totalOut.add(tx.getCantidad());
        }
    }

    public void addInvalid(String error) {
        invalidCount++;
        errors.add(error);
    }

    public void printSummary() {
        System.out.println("=== IMPORT SUMMARY ===");
        System.out.println("Valid: " + validCount);
        System.out.println("Invalid: " + invalidCount);
        System.out.println("Total IN: " + totalIn);
        System.out.println("Total OUT: " + totalOut);

        System.out.println("\nErrors:");
        errors.forEach(System.out::println);
    }
}