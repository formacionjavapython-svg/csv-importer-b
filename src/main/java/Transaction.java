public class Transaction {

    String id;
    TxType type;
    Money amount;
    String description;

    public Transaction(String id, TxType type, Money amount, String description) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }

    @Override
    public String toString() {
        return "ID=" + id +
                ", tipo=" + type +
                ", detalle='" + description + "'";
    }
}