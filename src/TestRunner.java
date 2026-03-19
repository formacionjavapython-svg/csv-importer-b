import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;

public class TestRunner {
    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;
        StringBuilder report = new StringBuilder("=== REPORTE DE PRUEBAS ===\n");

        System.out.println("Iniciando pruebas personalizadas...\n");

        // Caso 1: Bloqueo de Path Traversal
        try {
            System.out.print("Caso 1 (Bloqueo de Path Traversal): ");
            Importer importer = new Importer();
            importer.processCsv("../../../etc/passwd");
            System.out.println("FALLÓ (No bloqueó la ruta maliciosa)");
            report.append("Caso 1: FALLÓ\n");
            failed++;
        } catch (SecurityException e) {
            System.out.println("PASÓ (" + e.getMessage() + ")");
            report.append("Caso 1: PASÓ\n");
            passed++;
        } catch (Exception e) {
            System.out.println("FALLÓ (Excepción incorrecta: " + e.getMessage() + ")");
            report.append("Caso 1: FALLÓ (Excepción incorrecta)\n");
            failed++;
        }

        // Caso 2 y 3: Conteo Validos/Invalidos y Totales Correctos
        try {
            System.out.print("Caso 2 y 3 (Conteo y Totales del CSV): ");
            Importer importer = new Importer();
            ImportResult result = importer.processCsv("transacciones.csv");

            // Validamos contra los datos exactos del CSV que creamos
            boolean countsMatch = result.getValidCount() == 4 && result.getInvalidCount() == 2;
            boolean totalsMatch = result.getTotalIn().compareTo(new BigDecimal("1650.50")) == 0 &&
                    result.getTotalOut().compareTo(new BigDecimal("200.00")) == 0;

            if (countsMatch && totalsMatch) {
                System.out.println("PASÓ");
                report.append("Caso 2 y 3: PASÓ\n");
                passed++;
            } else {
                System.out.println("FALLÓ (Los contadores o sumas no coinciden)");
                report.append("Caso 2 y 3: FALLÓ\n");
                failed++;
            }
        } catch (Exception e) {
            System.out.println("FALLÓ (" + e.getMessage() + ")");
            report.append("Caso 2 y 3: FALLÓ por excepción\n");
            failed++;
        }

        // Generar el archivo de reporte solicitado
        report.append("--------------------------\n");
        report.append("Total Pasadas: ").append(passed).append("\n");
        report.append("Total Fallidas: ").append(failed).append("\n");

        try (PrintWriter writer = new PrintWriter(new FileWriter("test-report.txt"))) {
            writer.write(report.toString());
            System.out.println("\nReporte guardado en 'test-report.txt'");
        } catch (Exception e) {
            System.out.println("No se pudo guardar el reporte: " + e.getMessage());
        }

        // Si hay fallos, salimos con codigo de error para que GitHub Actions lo detecte
        if (failed > 0) {
            System.exit(1);
        }
    }
}