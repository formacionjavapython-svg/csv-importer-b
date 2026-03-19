package main.java;

import java.math.BigDecimal;

public class Money {
    private final BigDecimal amount;
    private final String currency;

    public Money(final String amountText, final String currencyText) {
        if (amountText == null || amountText.isBlank()) {
            throw new IllegalArgumentException("El monto no puede estar vacio");
        }
        if (currencyText == null || currencyText.isBlank()) {
            throw new IllegalArgumentException("La moneda no puede estar vacia");
        }

        this.amount = new BigDecimal(amountText);
        if (this.amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }

        this.currency = currencyText.trim().toUpperCase();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
