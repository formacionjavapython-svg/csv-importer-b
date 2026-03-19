public class Transaction {
    public enum TxType { IN, OUT }

    // Aqui tambien aplicamos el principio de encapsulamiento.
    private final String id;
    private final TxType type;
    private final Money money;
    private final String description;


    // Este constructor representa cada linea del CSV, para luego validar los datos, si algun dato es inválido, lanza una excepcion .
    public Transaction(String id, String typeStr, String amount, String currency, String description) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID inválido");
        this.id = id.trim();
        this.type = TxType.valueOf(typeStr.trim().toUpperCase());
        this.money = new Money(amount, currency);
        this.description = description != null ? description.trim() : "";
    }

    public TxType getType() { return type; }
    public Money getMoney() { return money; }
}