<<<<<<< HEAD
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
=======
import java.math.BigDecimal;

public class Money {
    private BigDecimal amount;
    private String currency;

    public Money(String amount, String currency) {
        this.amount = new BigDecimal(amount);
        this.currency = currency;
    }

    public BigDecimal getAmount() { return amount; }
}
>>>>>>> 9b6df89a2299982d679daada700f55ff0526305a
