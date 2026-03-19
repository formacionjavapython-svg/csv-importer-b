package dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ImportResult {

    private int validCount;
    private int invalidCount;

    private BigDecimal totalIn = BigDecimal.ZERO;
    private BigDecimal totalOut = BigDecimal.ZERO;

    private final List<String> errors = new ArrayList<>();

    public void addValid() {
        validCount++;
    }

    public void addInvalid(String error) {
        invalidCount++;
        errors.add(error);
    }

    public void addToIn(BigDecimal amount) {
        totalIn = totalIn.add(amount);
    }

    public void addToOut(BigDecimal amount) {
        totalOut = totalOut.add(amount);
    }

    @Override
    public String toString() {
        return "Valid: " + validCount +
                "\nInvalid: " + invalidCount +
                "\nTotal IN: " + totalIn +
                "\nTotal OUT: " + totalOut +
                "\nErrors: " + errors;
    }
}