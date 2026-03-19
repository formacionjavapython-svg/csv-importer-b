package com.axity.csvimporter.result;

import com.axity.csvimporter.model.Money;
import com.axity.csvimporter.model.Transaction;
import com.axity.csvimporter.model.TransactionType;

import java.math.BigDecimal;
import java.util.*;

public class ImportResult {
    private final List<Transaction> validTransactions;
    private final List<String> errors;
    private int validCount;
    private int invalidCount;

    // Cambiamos a Map para tener totales por moneda
    private final Map<String, Money> totalInByCurrency;
    private final Map<String, Money> totalOutByCurrency;

    private ImportResult() {
        this.validTransactions = new ArrayList<>();
        this.errors = new ArrayList<>();
        this.validCount = 0;
        this.invalidCount = 0;
        this.totalInByCurrency = new HashMap<>();
        this.totalOutByCurrency = new HashMap<>();
    }

    public static ImportResult empty() {
        return new ImportResult();
    }

    public void addValidTransaction(Transaction transaction) {
        validTransactions.add(transaction);
        validCount++;

        String currency = transaction.getMoney().getCurrency();
        Money money = transaction.getMoney();

        // Actualizar totales por moneda
        if (transaction.getType() == TransactionType.IN) {
            totalInByCurrency.merge(currency, money, (m1, m2) -> {
                try {
                    return m1.add(m2);
                } catch (IllegalArgumentException e) {
                    // Si hay error al sumar (no debería pasar con misma moneda)
                    System.err.println("Error sumando " + currency + ": " + e.getMessage());
                    return m1;
                }
            });
        } else { // OUT
            totalOutByCurrency.merge(currency, money, (m1, m2) -> {
                try {
                    return m1.add(m2);
                } catch (IllegalArgumentException e) {
                    System.err.println("Error sumando " + currency + ": " + e.getMessage());
                    return m1;
                }
            });
        }
    }

    public void addError(String error) {
        errors.add(error);
        invalidCount++;
    }

    public void addError(int lineNumber, String error) {
        errors.add("Línea " + lineNumber + ": " + error);
        invalidCount++;
    }

    public List<Transaction> getValidTransactions() {
        return Collections.unmodifiableList(validTransactions);
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public int getValidCount() {
        return validCount;
    }

    public int getInvalidCount() {
        return invalidCount;
    }

    public Map<String, Money> getTotalInByCurrency() {
        return Collections.unmodifiableMap(totalInByCurrency);
    }

    public Map<String, Money> getTotalOutByCurrency() {
        return Collections.unmodifiableMap(totalOutByCurrency);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public void printSummary() {
        System.out.println("\n=== RESUMEN DE IMPORTACIÓN ===");
        System.out.println("Registros válidos: " + validCount);
        System.out.println("Registros inválidos: " + invalidCount);

        System.out.println("\n--- INGRESOS POR MONEDA (IN) ---");
        if (totalInByCurrency.isEmpty()) {
            System.out.println("  No hay ingresos");
        } else {
            totalInByCurrency.forEach((currency, money) ->
                    System.out.println("  " + currency + ": " + money.getAmount()));
        }

        System.out.println("\n--- EGRESOS POR MONEDA (OUT) ---");
        if (totalOutByCurrency.isEmpty()) {
            System.out.println("  No hay egresos");
        } else {
            totalOutByCurrency.forEach((currency, money) ->
                    System.out.println("  " + currency + ": " + money.getAmount()));
        }

        // Totales globales (solo informativos, no sumamos monedas diferentes)
        BigDecimal totalInGlobal = totalInByCurrency.values().stream()
                .map(Money::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalOutGlobal = totalOutByCurrency.values().stream()
                .map(Money::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("\n--- TOTALES GLOBALES (suma aritmética, sin conversión) ---");
        System.out.println("Total IN (suma de montos): " + totalInGlobal);
        System.out.println("Total OUT (suma de montos): " + totalOutGlobal);

        if (hasErrors()) {
            System.out.println("\n--- ERRORES ENCONTRADOS (" + errors.size() + ") ---");
            for (String error : errors) {
                System.out.println("  • " + error);
            }
        }
        System.out.println("================================\n");
    }
}