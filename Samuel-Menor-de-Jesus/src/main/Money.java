package main;

public class Money {
    private double amount;
    private String currency;

    public Money(String amount, String currency) {
        setAmount(amount);
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    private void setAmount(String amountStr) {
        try {
            double amount = Double.parseDouble(amountStr);
            if (amount < 0) {
                throw new IllegalArgumentException("El monto no puede ser negativo.");
            }
            this.amount = amount;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato de monto inválido.");
        }
    }
}
