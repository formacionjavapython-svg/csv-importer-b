import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;

public class TestRunner {
    private int passed = 0;
    private int failed = 0;
    private StringBuilder report = new StringBuilder("=== REPORTE DE PRUEBAS ===\n");

    // Pruebas personalizadas para validar seguridad y lógica del CSV.
    public static void main(String[] args) {
        TestRunner tr = new TestRunner();
        System.out.println("Iniciando pruebas personalizadas...\n");
        tr.testSecurity();
        tr.testCsvLogic();
        tr.saveReport();

        if (tr.failed > 0) {
            System.exit(1);
        }
    }

    private void testSecurity() {
        try {
            System.out.print("Caso 1 (Bloqueo de Path Traversal): ");
            new Importer().processCsv("../../../etc/passwd");
            updateReport("Caso 1", "FALLÓ (No bloqueó ruta)", false);
        } catch (SecurityException e) {
            updateReport("Caso 1", "PASÓ (" + e.getMessage() + ")", true);
        } catch (Exception e) {
            updateReport("Caso 1", "FALLÓ (Excepción incorrecta)", false);
        }
    }

    private void testCsvLogic() {
        try {
            System.out.print("Caso 2 y 3 (Conteo y Totales del CSV): ");
            ImportResult result = new Importer().processCsv("transacciones.csv");
            boolean cMatch = result.getValidCount() == 4 && result.getInvalidCount() == 2;
            boolean tMatch = result.getTotalIn().compareTo(new BigDecimal("1650.50")) == 0 &&
                    result.getTotalOut().compareTo(new BigDecimal("200.00")) == 0;
            if (cMatch && tMatch) {
                updateReport("Caso 2 y 3", "PASÓ", true);
            } else {
                updateReport("Caso 2 y 3", "FALLÓ (Sumas no coinciden)", false);
            }
        } catch (Exception e) {
            updateReport("Caso 2 y 3", "FALLÓ por excepción", false);
        }
    }

    private void updateReport(String caso, String msj, boolean isPassed) {
        System.out.println(msj);
        report.append(caso).append(": ").append(msj).append("\n");
        if (isPassed) { passed++; } else { failed++; }
    }
    private void saveReport() {
        report.append("--------------------------\n");
        report.append("Total Pasadas: ").append(passed).append("\n");
        report.append("Total Fallidas: ").append(failed).append("\n");
        try (PrintWriter writer = new PrintWriter(new FileWriter("test-report.txt"))) {
            writer.write(report.toString());
            System.out.println("\nReporte guardado en 'test-report.txt'");
        } catch (Exception e) {
            System.out.println("Error al guardar reporte: " + e.getMessage());
        }
    }
}