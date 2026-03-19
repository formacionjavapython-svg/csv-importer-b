package main.java;

import java.math.BigDecimal;

public class Money {
    private final BigDecimal amount;
    private final String currency;

    public Money(String amount, String currency) {
        if (amount == null || amount.isBlank()) {
            throw new IllegalArgumentException("El monto no puede estar vacío");
        }
        this.amount = new BigDecimal(amount);
        this.currency = currency;
    }

    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
}