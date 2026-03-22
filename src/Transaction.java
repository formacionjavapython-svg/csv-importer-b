public final class Transaction {
    private final String id;
    private final TxType type;
    private final Money money;

    public Transaction(String id, TxType type, Money money) {
        this.id = id;
        this.type = type;
        this.money = money;
    }

    public TxType getType() { return type; }
    public Money getMoney() { return money; }
}