import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class TestRunner {
    public static void main(final String[] args) {
        final StringBuilder report = new StringBuilder();
        try {
            final Importer imp = new Importer();
            final ImportResult res = imp.processFile("transacciones.csv");

            report.append("ESTADO: EXITOSO\n");
            report.append("REGISTROS: ").append(res.getValidCount());
            System.out.println("✅ Reporte generado con éxito.");
        } catch (Exception e) {
            report.append("ESTADO: ERROR - ").append(e.getMessage());
            System.err.println("❌ Error: " + e.getMessage());
        }

        try (PrintWriter out = new PrintWriter("test-report.txt", StandardCharsets.UTF_8)) {
            out.println(report.toString());
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}