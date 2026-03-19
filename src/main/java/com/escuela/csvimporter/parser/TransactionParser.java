package com.escuela.csvimporter.parser;

import com.escuela.csvimporter.model.Money;
import com.escuela.csvimporter.model.Transaction;
import com.escuela.csvimporter.model.TxType;
import java.math.BigDecimal;

public class TransactionParser {

    public Transaction parse(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Línea vacía");
        }

        String[] parts = line.split(",");

        if (parts.length != 5) {
            throw new IllegalArgumentException("La línea debe tener 5 columnas");
        }

        String id = parts[0].trim();
        String typeText = parts[1].trim();
        String amountText = parts[2].trim();
        String currency = parts[3].trim();
        String description = parts[4].trim();

        TxType type = TxType.valueOf(typeText);
        BigDecimal amount = new BigDecimal(amountText);
        Money money = new Money(amount, currency);

        return new Transaction(id, type, money, description);
    }
}