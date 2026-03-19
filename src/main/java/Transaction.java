package main.java;

public class Transaction {

    private final String id;
    private final TxType type;
    private final Money money;
    private final String description;

    public Transaction(
            final String idText,
            final TxType typeValue,
            final Money moneyValue,
            final String descriptionText) {
        if (idText == null || idText.isBlank()) {
            throw new IllegalArgumentException("El id no puede estar vacio");
        }
        if (typeValue == null) {
            throw new IllegalArgumentException("El tipo no puede ser nulo");
        }
        if (moneyValue == null) {
            throw new IllegalArgumentException("El dinero no puede ser nulo");
        }
        if (descriptionText == null || descriptionText.isBlank()) {
            throw new IllegalArgumentException("La descripcion no puede estar vacia");
        }

        this.id = idText.trim();
        this.type = typeValue;
        this.money = moneyValue;
        this.description = descriptionText.trim();
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

    @Override
    public String toString() {
        return "Transaction{id='" + id + "', type=" + type
                + ", money=" + money + ", description='" + description + "'}";
    }
}
