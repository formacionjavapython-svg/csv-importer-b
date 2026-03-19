public class Transaction {
    private String id;
    private String type; // IN o OUT
    private Money money;
    private String description;

    public Transaction(String id, String type, Money money, String description) {
        if (!type.equals("IN") && !type.equals("OUT")) {
            throw new IllegalArgumentException("Tipo inválido: debe ser IN o OUT");
        }
        this.id = id;
        this.type = type;
        this.money = money;
        this.description = description;
    }

    public String getId() { return id; }
    public String getType() { return type; }
    public Money getMoney() { return money; }
    public String getDescription() { return description; }
}
