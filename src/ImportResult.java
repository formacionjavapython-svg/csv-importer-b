import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ImportResult {
    private int validCount = 0;
    private int invalidCount = 0;
    private BigDecimal totalIn = BigDecimal.ZERO;
    private BigDecimal totalOut = BigDecimal.ZERO;
    private final List<String> errors = new ArrayList<>();

    /* * Almacena el resultado de la importación: conteos,
     * totales de ingresos/egresos y lista de errores.
     */

    public void addValid(Transaction tx) {
        this.validCount++;
        if (tx.getType() == Transaction.TxType.IN) {
            this.totalIn = this.totalIn.add(tx.getMoney().getAmount());
        } else {
            this.totalOut = this.totalOut.add(tx.getMoney().getAmount());
        }
    }

    public void addInvalid(String error) {
        this.invalidCount++;
        this.errors.add(error);
    }

    public void printSummary() {
        System.out.println("--- Resumen de Importación ---");
        System.out.println("Registros válidos: " + validCount);
        System.out.println("Registros inválidos: " + invalidCount);
        System.out.println("Total Ingresos (IN): " + totalIn);
        System.out.println("Total Egresos (OUT): " + totalOut);
        if (!errors.isEmpty()) {
            System.out.println("Errores detectados durante el proceso:");
            errors.forEach(e -> System.out.println(" - " + e));
        }
    }

    public int getValidCount() { return validCount; }
    public int getInvalidCount() { return invalidCount; }
    public BigDecimal getTotalIn() { return totalIn; }
    public BigDecimal getTotalOut() { return totalOut; }
}