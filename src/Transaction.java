/** Clase que representa una transacción. */
public class Transaction {
    private final TxType type;
    private final Money money;

    public Transaction(final TxType type, final Money money) {
        this.type = type;
        this.money = money;
    }

    public final TxType getType() { return type; }
    public final Money getMoney() { return money; }
}