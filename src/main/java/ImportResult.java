package main.java;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImportResult {
    private int validCount = 0;
    private int invalidCount = 0;
    private BigDecimal totalIn = BigDecimal.ZERO;
    private BigDecimal totalOut = BigDecimal.ZERO;
    private final List<String> errors = new ArrayList<>();

    public void addValid(Transaction tx) {
        validCount++;
        if (tx.getType() == Transaction.TxType.IN) {
            totalIn = totalIn.add(tx.getMoney().getAmount());
        } else {
            totalOut = totalOut.add(tx.getMoney().getAmount());
        }
    }

    public void addInvalid(String error) {
        invalidCount++;
        errors.add(error);
    }

    public int getValidCount() {
        return validCount;
    }

    public int getInvalidCount() {
        return invalidCount;
    }

    public BigDecimal getTotalIn() {
        return totalIn;
    }

    public BigDecimal getTotalOut() {
        return totalOut;
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}