import java.math.BigDecimal;

public class Money {
    // Encapsulamiento de monto y moneda, con validaciones en el constructor.
    private final BigDecimal amount;
    private final String currency;


    public Money(String amount, String currency) {
        if (amount == null || amount.isBlank()) {
            throw new IllegalArgumentException("Monto inválido");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Moneda inválida");
        }

        this.amount = new BigDecimal(amount.trim());
        this.currency = currency.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}