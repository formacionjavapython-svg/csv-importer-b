import java.util.*;

public class ImportResult {
    private int validCount;
    private int invalidCount;
    private double totalIn;
    private double totalOut;
    private List<String> errors;

    public ImportResult() {
        this.errors = new ArrayList<>();
    }

    public void addValid(Transaction t) {
        validCount++;
        if (t.getType().equals("IN")) {
            totalIn += t.getMoney().getAmount();
        } else {
            totalOut += t.getMoney().getAmount();
        }
    }

    public void addInvalid(String error) {
        invalidCount++;
        errors.add(error);
    }

    public void printSummary() {
        System.out.println("Resumen del proceso:");
        System.out.println("Válidos: " + validCount);
        System.out.println("Inválidos: " + invalidCount);
        System.out.println("Total IN: " + totalIn);
        System.out.println("Total OUT: " + totalOut);
        System.out.println("Errores: " + errors);
    }
}