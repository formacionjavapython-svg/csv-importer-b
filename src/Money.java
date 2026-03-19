import java.math.BigDecimal;

public class Money {
    private final BigDecimal amount;
    private final String currency;// Aplicamos inmutabillidad con final

    public Money(String amount, String currency) {
        if (amount == null || amount.isBlank()) throw new IllegalArgumentException("Monto inválido");
        if (currency == null || currency.isBlank()) throw new IllegalArgumentException("Moneda inválida");

        this.amount = new BigDecimal(amount.trim());
        this.currency = currency.trim();
    }
// Aplicamos el principio de encapsulamiento, por lo que los atributos son privados y solo se pueden acceder a través de métodos públicos.
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
}