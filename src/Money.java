import java.math.BigDecimal;

/** Manejo de montos monetarios. */
public class Money {
    private final BigDecimal amount;
    private final String currency;

    public Money(final String amount, final String currency) {
        this.amount = new BigDecimal(amount);
        this.currency = currency;
    }

    public final BigDecimal getAmount() { return amount; }
    public final String getCurrency() { return currency; }
}