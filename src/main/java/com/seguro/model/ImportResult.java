package com.seguro.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ImportResult {

    private int validCount;
    private int invalidCount;
    private BigDecimal totalIn;
    private BigDecimal totalOut;
    private final List<String> errors;

    public ImportResult() {
        this.validCount = 0;
        this.invalidCount = 0;
        this.totalIn = BigDecimal.ZERO;
        this.totalOut = BigDecimal.ZERO;
        this.errors = new ArrayList<>();
    }

    public void addValid(Transaction transaction) {
        validCount++;

        if (transaction.getType() == TxType.IN) {
            totalIn = totalIn.add(transaction.getMoney().getAmount());
        } else {
            totalOut = totalOut.add(transaction.getMoney().getAmount());
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
        return errors;
    }
}