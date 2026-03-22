<<<<<<< HEAD
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
            final ImportResult result = importer.processFile("data/transacciones.csv");

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
=======
public class TestManual {
    public static void main(String[] args) {
        System.out.println("--- EJECUTANDO PRUEBAS DE VALIDACIÓN ---");
        Importer imp = new Importer();

        // Prueba 1: Archivo que NO existe
        try {
            imp.processFile("archivo_fantasma.csv");
            System.out.println(" Falló: Debería haber lanzado error de archivo no encontrado.");
        } catch (Exception e) {
            System.out.println("✅ Pasó: Detectó correctamente que el archivo no existe.");
        }

        // Prueba 2: Intento de salir de la carpeta (Seguridad)
        try {
            imp.processFile("../config.xml");
            System.out.println(" Falló: Debería haber bloqueado el acceso fuera de /data.");
        } catch (SecurityException e) {
            System.out.println(" Pasó: Bloqueó el intento de Path Traversal.");
        } catch (Exception e) {
            System.out.println("⚠ Nota: Error inesperado pero bloqueado: " + e.getMessage());
        }
    }
>>>>>>> 9b6df89a2299982d679daada700f55ff0526305a
}