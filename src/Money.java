public class Money {
    public double amount;
    public String currency;

    public Money(double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public String toString() {
        return amount + " " + currency;
    }
}
