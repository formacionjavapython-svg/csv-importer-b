import java.math.BigDecimal;

public class ImportResult {
    int validos = 0;
    int invalidos = 0;
    BigDecimal totalIngresos = BigDecimal.ZERO;
    BigDecimal totalEgresos = BigDecimal.ZERO;

    public void imprimirResumen() {
        System.out.println("Válidos : " + validos);
        System.out.println("Inválidos : " + invalidos);
        System.out.println("Ingresos : " + totalIngresos);
        System.out.println("Egresos : " + totalEgresos);
    }
}
