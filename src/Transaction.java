/**
 * Representa una transacción individual del CSV.
 */
public class Transaction {
    private final TxType type;
    private final Money money;

    public Transaction(final TxType type, final Money money) {
        this.type = type;
        this.money = money;
    }

    public final TxType getType() {
        return type;
    }

    public final Money getMoney() {
        return money;
    }
}