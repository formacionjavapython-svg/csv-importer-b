package importer;



import model.Money;
import model.Transaccion;

import java.math.BigDecimal;
import java.util.*;

public class ImportResult {
    private final int registrosValidos;
    private final int registrosInvalidos;
    private final Map<String, Money> ingresosPorMoneda;  // Total IN por moneda
    private final Map<String, Money> egresosPorMoneda;   // Total OUT por moneda
    private final List<String> errores;

    // Constructor privado
    private ImportResult(int registrosValidos, int registrosInvalidos,
                         Map<String, Money> ingresosPorMoneda,
                         Map<String, Money> egresosPorMoneda,
                         List<String> errores) {
        this.registrosValidos = registrosValidos;
        this.registrosInvalidos = registrosInvalidos;
        this.ingresosPorMoneda = Collections.unmodifiableMap(new HashMap<>(ingresosPorMoneda));
        this.egresosPorMoneda = Collections.unmodifiableMap(new HashMap<>(egresosPorMoneda));
        this.errores = Collections.unmodifiableList(new ArrayList<>(errores));
    }

    // Builder para construir el resultado (patrón Builder)
    public static class Builder {
        private int registrosValidos = 0;
        private int registrosInvalidos = 0;
        private final Map<String, Money> ingresosPorMoneda = new HashMap<>();
        private final Map<String, Money> egresosPorMoneda = new HashMap<>();
        private final List<String> errores = new ArrayList<>();

        public Builder agregarTransaccionValida(Transaccion t) {
            registrosValidos++;

            // Acumular por moneda según tipo
            String moneda = t.getMonto().getMoneda();
            if (t.esIngreso()) {
                ingresosPorMoneda.merge(moneda, t.getMonto(), Money::sumar);
            } else {
                egresosPorMoneda.merge(moneda, t.getMonto(), Money::sumar);
            }

            return this;
        }

        public Builder agregarError(String error) {
            registrosInvalidos++;
            errores.add(error);
            return this;
        }

        public Builder agregarLineaInvalida(String linea, String razon) {
            registrosInvalidos++;
            errores.add(String.format("Línea inválida [%s]: %s", razon, linea));
            return this;
        }

        public ImportResult build() {
            return new ImportResult(registrosValidos, registrosInvalidos,
                    ingresosPorMoneda, egresosPorMoneda, errores);
        }
    }

    // Método de utilidad para crear builder
    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public int getRegistrosValidos() {
        return registrosValidos;
    }

    public int getRegistrosInvalidos() {
        return registrosInvalidos;
    }

    public Map<String, Money> getIngresosPorMoneda() {
        return ingresosPorMoneda;
    }

    public Map<String, Money> getEgresosPorMoneda() {
        return egresosPorMoneda;
    }

    public List<String> getErrores() {
        return errores;
    }

    // Métodos de utilidad para obtener totales (por si necesitas el total general)
    public BigDecimal getTotalIngresosEn(String moneda) {
        Money money = ingresosPorMoneda.get(moneda.toUpperCase());
        return money != null ? money.getCantidad() : BigDecimal.ZERO;
    }

    public BigDecimal getTotalEgresosEn(String moneda) {
        Money money = egresosPorMoneda.get(moneda.toUpperCase());
        return money != null ? money.getCantidad() : BigDecimal.ZERO;
    }

    // Generar reporte formateado (como pide el criterio de aceptación)
    public void generarReporte() {
        System.out.println("=".repeat(60));
        System.out.println("RESUMEN DE IMPORTACIÓN");
        System.out.println("=".repeat(60));

        System.out.printf("Registros válidos:   %d%n", registrosValidos);
        System.out.printf("Registros inválidos: %d%n", registrosInvalidos);

        if (!ingresosPorMoneda.isEmpty()) {
            System.out.println("\n📈 INGRESOS (IN) por moneda:");
            ingresosPorMoneda.forEach((moneda, monto) ->
                    System.out.printf("  • %s: %s%n", moneda, monto));
        }

        if (!egresosPorMoneda.isEmpty()) {
            System.out.println("\n📉 EGRESOS (OUT) por moneda:");
            egresosPorMoneda.forEach((moneda, monto) ->
                    System.out.printf("  • %s: %s%n", moneda, monto));
        }

        if (!errores.isEmpty()) {
            System.out.println("\n❌ ERRORES ENCONTRADOS:");
            errores.forEach(error -> System.out.println("  • " + error));
        }

        System.out.println("=".repeat(60));
    }

    @Override
    public String toString() {
        return String.format("ImportResult{validos=%d, invalidos=%d, ingresos=%d monedas, egresos=%d monedas, errores=%d}",
                registrosValidos, registrosInvalidos,
                ingresosPorMoneda.size(), egresosPorMoneda.size(),
                errores.size());
    }
}
