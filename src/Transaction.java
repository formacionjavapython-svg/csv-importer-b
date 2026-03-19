public class Transaction {
    public String id;
    public String type;
    public Money money;
    public String description;

    public Transaction(String id, String type, Money money, 
                      String description) {
        this.id = id;
        this.type = type;
        this.money = money;
        this.description = description;
    }

    public String toString() {
        return id + " | " + type + " | " + money + " | " + 
               description;
    }
}
