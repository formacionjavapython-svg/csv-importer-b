public class Money {
    private double amount;
    private String currency;

    public Money(String amountStr, String currency) {
        double parsedAmount = Double.parseDouble(amountStr);
        if (parsedAmount < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        this.amount = parsedAmount;
        this.currency = currency;
    }

    public double getAmount() { return amount; }
    public String getCurrency() { return currency; }
}