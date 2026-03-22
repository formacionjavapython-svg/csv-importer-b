import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Punto de entrada para validación y generación de reporte de entrega.
 */
public class TestManual {
    public static void main(final String[] args) {
        final StringBuilder report = new StringBuilder();
        report.append("--- REPORTE DE LABORATORIO AXITY ---\n");

        try {
            final Importer importer = new Importer();
            // Asegúrate de que transacciones.csv esté en la carpeta indicada por IMPORT_BASE_DIR
            final ImportResult result = importer.processFile("transacciones.csv");

            report.append("RESULTADO: EXITOSO\n");
            report.append("Registros Procesados: ").append(result.getValidCount()).append("\n");
            report.append("Total IN: ").append(result.getTotalIn()).append("\n");
            report.append("Total OUT: ").append(result.getTotalOut()).append("\n");

            System.out.println("✅ Reporte generado correctamente.");

        } catch (final Exception e) {
            report.append("RESULTADO: ERROR\n");
            report.append("Detalle: ").append(e.getMessage()).append("\n");
            System.err.println("❌ Fallo en la ejecución: " + e.getMessage());
        }

        // Generación del archivo físico test-report.txt
        try (PrintWriter writer = new PrintWriter("test-report.txt", StandardCharsets.UTF_8)) {
            writer.println(report.toString());
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }
}