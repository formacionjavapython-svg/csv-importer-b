package main.java;




import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class ImportResult {
    private final int validCount;
    private final int invalidCount;
    private final Money totalIn;
    private final Money totalOut;
    private final List<String> errors;

    private ImportResult(int validCount, int invalidCount, Money totalIn, Money totalOut, List<String> errors) {
        this.validCount = validCount;
        this.invalidCount = invalidCount;
        this.totalIn = totalIn;
        this.totalOut = totalOut;
        this.errors = Collections.unmodifiableList(new ArrayList<>(errors));
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getValidCount() {
        return validCount;
    }

    public int getInvalidCount() {
        return invalidCount;
    }

    public Money getTotalIn() {
        return totalIn;
    }

    public Money getTotalOut() {
        return totalOut;
    }

    public List<String> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return String.format("ImportResult{valid=%d, invalid=%d, totalIn=%s, totalOut=%s, errors=%d}",
                validCount, invalidCount, totalIn, totalOut, errors.size());
    }


    public static class Builder {
        private int validCount = 0;
        private int invalidCount = 0;
        private Money totalIn = null;
        private Money totalOut = null;
        private final List<String> errors = new ArrayList<>();

        private Builder() {}

        public Builder addValid(Transaction tx) {
            validCount++;
            if (tx.getType() == TxType.IN) {
                totalIn = (totalIn == null) ? tx.getMoney() : totalIn.add(tx.getMoney());
            } else {
                totalOut = (totalOut == null) ? tx.getMoney() : totalOut.add(tx.getMoney());
            }
            return this;
        }

        public Builder addInvalid(String error) {
            invalidCount++;
            errors.add(error);
            return this;
        }

        public ImportResult build() {
          
            String currency = "MXN";
            if (totalIn == null && totalOut != null) {
                currency = totalOut.getCurrency();
            } else if (totalOut == null && totalIn != null) {
                currency = totalIn.getCurrency();
            } else if (totalIn != null && totalOut != null) {
                if (!totalIn.getCurrency().equals(totalOut.getCurrency())) {
                    throw new IllegalStateException("Las transacciones tienen monedas diferentes: IN=" + totalIn.getCurrency() + ", OUT=" + totalOut.getCurrency());
                }
                currency = totalIn.getCurrency();
            }
            Money zero = new Money("0.00", currency);
            return new ImportResult(
                    validCount,
                    invalidCount,
                    totalIn != null ? totalIn : zero,
                    totalOut != null ? totalOut : zero,
                    errors
            );
        }
    }
}