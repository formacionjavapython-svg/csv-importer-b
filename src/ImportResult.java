<<<<<<< HEAD
import java.math.BigDecimal;

/** Resultados del procesamiento. */
public class ImportResult {
    private int validCount = 0;
    private BigDecimal totalIn = BigDecimal.ZERO;
    private BigDecimal totalOut = BigDecimal.ZERO;

    public final int getValidCount() { return validCount; }
    public final void setValidCount(final int v) { this.validCount = v; }
    public final BigDecimal getTotalIn() { return totalIn; }
    public final void setTotalIn(final BigDecimal t) { this.totalIn = t; }
    public final BigDecimal getTotalOut() { return totalOut; }
    public final void setTotalOut(final BigDecimal t) { this.totalOut = t; }
}
=======
import java.math.BigDecimal;

public class ImportResult {
    public int validCount = 0;
    public BigDecimal totalIn = BigDecimal.ZERO;
    public BigDecimal totalOut = BigDecimal.ZERO;
}
>>>>>>> 9b6df89a2299982d679daada700f55ff0526305a
