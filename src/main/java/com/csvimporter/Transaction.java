package com.csvimporter;

import java.util.Objects;

/**
 * Representa una transacción financiera con un identificador único, un tipo (ingreso o egreso),
 * un monto monetario y una descripción.
 *
 * <p>Las instancias de esta clase son inmutables y se validan durante la construcción para
 * garantizar la integridad de los datos y la consistencia del sistema.</p>
 */
public final class Transaction {
    /**
     * Identificador único de la transacción.
     */
    private final String transactionId;
    /**
     * Tipo de la transacción (IN o OUT).
     */
    private final TxType transactionType;
    /**
     * Monto monetario asociado a la transacción.
     */
    private final Money monetaryAmount;
    /**
     * Descripción de la transacción.
     */
    private final String transactionDescription;

    /**
     * Construye una nueva transacción con los datos especificados.
     *
     * @param transactionId          el identificador único de la transacción; no puede ser {@code null} ni
     *                               una cadena vacía
     * @param transactionType        el tipo de la transacción, debe ser {@link TxType#IN} o {@link TxType#OUT};
     *                               no puede ser {@code null}
     * @param monetaryAmount         el monto monetario de la transacción; no puede ser {@code null}
     * @param transactionDescription la descripción de la transacción; puede ser {@code null} o cadena vacía
     * @throws IllegalArgumentException si alguno de los parámetros obligatorios es inválido
     */
    public Transaction(String transactionId, TxType transactionType, Money monetaryAmount, String transactionDescription) {
        Objects.requireNonNull(transactionId, "El identificador de la transacción no puede ser nulo");
        if (transactionId.isBlank()) {
            throw new IllegalArgumentException("El identificador de la transacción no puede estar vacío");
        }
        Objects.requireNonNull(transactionType, "El tipo de la transacción no puede ser nulo");
        Objects.requireNonNull(monetaryAmount, "El monto de la transacción no puede ser nulo");
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.monetaryAmount = monetaryAmount;
        // Si la descripción es nula, almacenamos una cadena vacía para evitar valores null
        this.transactionDescription = transactionDescription == null ? "" : transactionDescription;
    }

    /**
     * Devuelve el identificador único de la transacción.
     *
     * @return el identificador como cadena
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Devuelve el tipo de la transacción.
     *
     * @return el tipo de la transacción
     */
    public TxType getTransactionType() {
        return transactionType;
    }

    /**
     * Devuelve el monto monetario asociado a la transacción.
     *
     * @return el objeto {@link Money} que representa el monto
     */
    public Money getMonetaryAmount() {
        return monetaryAmount;
    }

    /**
     * Devuelve la descripción de la transacción.
     *
     * @return la descripción, nunca nula (puede ser cadena vacía)
     */
    public String getTransactionDescription() {
        return transactionDescription;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", transactionType=" + transactionType +
                ", monetaryAmount=" + monetaryAmount +
                ", transactionDescription='" + transactionDescription + '\'' +
                '}';
    }
}