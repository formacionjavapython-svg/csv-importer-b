public final class Transaction {
    private final TxType type;
    private final Money money;

    public Transaction( TxType type, Money money) {

        this.type = type;
        this.money = money;
    }

    public TxType getType() { return type; }
    public Money getMoney() { return money; }
}