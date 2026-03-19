package main.java;

import java.util.ArrayList;
import java.util.List;

public final class ImportResult {
    private final int validCount;
    private final int invalidCount;
    private final Money totalIn;
    private final Money totalOut;
    private final List<String> errors;

    public ImportResult(int validCount, int invalidCount, Money totalIn,
                        Money totalOut, List<String> errors) {
        this.validCount = validCount;
        this.invalidCount = invalidCount;
        this.totalIn = totalIn;
        this.totalOut = totalOut;
        this.errors = new ArrayList<>(errors);
    }

    public int getValidCount() {
        return validCount;
    }

    public int getInvalidCount() {
        return invalidCount;
    }

    public Money getTotalIn() {
        return totalIn;
    }

    public Money getTotalOut() {
        return totalOut;
    }

    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }

    public void printSummary() {
        System.out.println("=== Import Summary ===");
        System.out.println("Valid records: " + validCount);
        System.out.println("Invalid records: " + invalidCount);
        System.out.println("Total IN: " + (totalIn != null ? totalIn : "0.00"));
        System.out.println("Total OUT: " + (totalOut != null ? totalOut : "0.00"));

        if (!errors.isEmpty()) {
            System.out.println("\nErrors:");
            for (String error : errors) {
                System.out.println("  - " + error);
            }
        }
    }
}