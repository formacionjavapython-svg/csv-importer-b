package main.java;

public enum TxType {
    IN,
    OUT;

    public static TxType fromString(String type) {
        if (type == null || type.isBlank()) {
            return null;
        }
        try {
            return TxType.valueOf(type.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
