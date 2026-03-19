package axel_hernandez;

public record Transaction(
    String id,
    TxType type,
    Money money,
    String description
) {}

enum TxType { IN, OUT }
