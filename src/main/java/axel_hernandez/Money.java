package axel_hernandez;

import java.math.BigDecimal;

public record Money(BigDecimal amount, String currency) {
    public Money {
        if (amount == null || currency == null) {
            throw new IllegalArgumentException("Monto y moneda no pueden ser nulos");
        }
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
