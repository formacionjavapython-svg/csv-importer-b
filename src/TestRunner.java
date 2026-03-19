
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import main.java.ImportResult;
import main.java.Importer;

public class TestRunner {
    private int passed;
    private int failed;

    public static void main(final String[] args) {
        final TestRunner runner = new TestRunner();
        runner.runAllTests();
    }

    public void runAllTests() {
        passed = 0;
        failed = 0;

        runTest("Caso 1: Bloqueo de Path Traversal", this::testPathTraversalBlocked);
        runTest("Caso 2: Conteo Validos/Invalidos", this::testValidInvalidCount);
        runTest("Caso 3: Totales Correctos", this::testCorrectTotals);

        printReport();
        writeReportFile();
    }

    private void runTest(final String testName, final TestCase testCase) {
        try {
            testCase.execute();
            passed++;
            System.out.println("[PASS] " + testName);
        } catch (Exception exception) {
            failed++;
            System.out.println("[FAIL] " + testName + " -> " + exception.getMessage());
        }
    }

    private void testPathTraversalBlocked() throws IOException {
        final Importer importer = new Importer();

        try {
            importer.validatePath("../etc/passwd");
            throw new IllegalStateException("No se bloqueo ../etc/passwd");
        } catch (SecurityException exception) {
            // correcto
        }

        try {
            importer.validatePath("C:\\Windows\\System32");
            throw new IllegalStateException("No se bloqueo ruta absoluta peligrosa");
        } catch (SecurityException | IOException exception) {
            // correcto
        }
    }

    private void testValidInvalidCount() {
        final Importer importer = new ImporterForTests("testdata");
        final ImportResult result = importer.importCsv("valid-invalid.csv");

        assertEquals(3, result.getValidCount(), "Validos incorrectos");
        assertEquals(2, result.getInvalidCount(), "Invalidos incorrectos");
    }

    private void testCorrectTotals() {
        final Importer importer = new ImporterForTests("testdata");
        final ImportResult result = importer.importCsv("totals.csv");

        assertEquals("300.00", result.getTotalIn().toString(), "Total IN incorrecto");
        assertEquals("80.00", result.getTotalOut().toString(), "Total OUT incorrecto");
    }

    private void assertEquals(
            final int expected,
            final int actual,
            final String message) {
        if (expected != actual) {
            throw new IllegalStateException(
                    message + ". Esperado: " + expected + ", actual: " + actual);
        }
    }

    private void assertEquals(
            final String expected,
            final String actual,
            final String message) {
        if (!expected.equals(actual)) {
            throw new IllegalStateException(
                    message + ". Esperado: " + expected + ", actual: " + actual);
        }
    }

    private void printReport() {
        System.out.println("=== REPORTE DE PRUEBAS ===");
        System.out.println("Pruebas aprobadas: " + passed);
        System.out.println("Pruebas fallidas: " + failed);
    }

    private void writeReportFile() {
        final String content = "Pruebas aprobadas: " + passed
                + System.lineSeparator()
                + "Pruebas fallidas: " + failed
                + System.lineSeparator();

        try {
            Files.writeString(Path.of("test-report.txt"), content);
            System.out.println("Archivo de reporte generado: test-report.txt");
        } catch (IOException exception) {
            System.out.println("No se pudo escribir test-report.txt: " + exception.getMessage());
        }
    }

    @FunctionalInterface
    interface TestCase {
        void execute() throws Exception;
    }
}
