import java.math.BigDecimal;

public class Money {
    private final BigDecimal amount;
    private final String currency;

    public Money(String amount, String currency) {
        this.amount = new BigDecimal(amount);
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
