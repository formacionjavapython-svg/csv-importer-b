package com.csvimporter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Acumula los resultados del proceso de importación.
 *
 * <p>Registra cuántas transacciones son válidas e inválidas, los totales de ingreso y egreso,
 * y la lista de mensajes de error asociados a registros inválidos.</p>
 */
public final class ImportResult {
    /**
     * Conteo de transacciones válidas procesadas.
     */
    private int validTransactionCount;
    /**
     * Conteo de transacciones inválidas procesadas.
     */
    private int invalidTransactionCount;
    /**
     * Suma total de los montos de tipo IN (ingresos).
     */
    private BigDecimal totalIncome;
    /**
     * Suma total de los montos de tipo OUT (egresos).
     */
    private BigDecimal totalExpense;
    /**
     * Lista de mensajes de error asociados a transacciones inválidas.
     */
    private final List<String> errorMessages;

    /**
     * Crea un nuevo resultado vacío.
     */
    public ImportResult() {
        this.validTransactionCount = 0;
        this.invalidTransactionCount = 0;
        this.totalIncome = BigDecimal.ZERO;
        this.totalExpense = BigDecimal.ZERO;
        this.errorMessages = new ArrayList<>();
    }

    /**
     * Registra una transacción válida y acumula el total.
     *
     * @param tx transacción válida a registrar
     */
    public void registerValidTransaction(Transaction transaction) {
        Objects.requireNonNull(transaction, "La transacción no puede ser nula");
        validTransactionCount++;
        BigDecimal amount = transaction.getMonetaryAmount().getAmount();
        if (transaction.getTransactionType() == TxType.IN) {
            totalIncome = totalIncome.add(amount);
        } else {
            totalExpense = totalExpense.add(amount);
        }
    }

    /**
     * Registra un error de transacción inválida.
     *
     * @param error mensaje descriptivo del error
     */
    public void registerError(String errorMessage) {
        invalidTransactionCount++;
        errorMessages.add(errorMessage);
    }

    /**
     * Obtiene el número de transacciones válidas.
     *
     * @return número de registros válidos
     */
    public int getValidTransactionCount() {
        return validTransactionCount;
    }

    /**
     * Obtiene el número de transacciones inválidas.
     *
     * @return número de registros inválidos
     */
    public int getInvalidTransactionCount() {
        return invalidTransactionCount;
    }

    /**
     * Obtiene el total de ingresos.
     *
     * @return suma de montos con tipo IN
     */
    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    /**
     * Obtiene el total de egresos.
     *
     * @return suma de montos con tipo OUT
     */
    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    /**
     * Devuelve una lista inmodificable de los errores encontrados.
     *
     * @return lista de mensajes de error
     */
    public List<String> getErrorMessages() {
        return Collections.unmodifiableList(errorMessages);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ImportResult{")
            .append("validTransactionCount=").append(validTransactionCount)
            .append(", invalidTransactionCount=").append(invalidTransactionCount)
            .append(", totalIncome=").append(totalIncome)
            .append(", totalExpense=").append(totalExpense)
            .append(", errorMessages=").append(errorMessages)
            .append('}');
        return sb.toString();
    }
}