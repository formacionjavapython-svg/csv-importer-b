package main.java;
public final class Transaction {
    private final String id;
    private final TxType type;
    private final Money money;
    private final String description;

    public Transaction(String id, TxType type, Money money, String description) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        if (money == null) {
            throw new IllegalArgumentException("Money cannot be null");
        }

        this.id = id.trim();
        this.type = type;
        this.money = money;
        this.description = description != null ? description.trim() : "";
    }

    public static Transaction parseLine(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }

        String[] parts = line.split(",", -1);
        if (parts.length < 5) {
            return null;
        }

        String id = parts[0].trim();
        TxType type = TxType.fromString(parts[1]);
        Money money = Money.parse(parts[2], parts[3]);
        String description = parts[4];

        if (id.isEmpty() || type == null || money == null) {
            return null;
        }

        return new Transaction(id, type, money, description);
    }

    public String getId() {
        return id;
    }

    public TxType getType() {
        return type;
    }

    public Money getMoney() {
        return money;
    }

    public String getDescription() {
        return description;
    }
}