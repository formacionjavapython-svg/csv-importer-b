package com.csvimporter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Pequeño framework de pruebas sin dependencias externas.
 *
 * <p>Define y ejecuta casos de prueba específicos para validar el comportamiento del importador
 * de CSV, incluyendo la detección de path traversal, el conteo de registros válidos e inválidos y
 * la suma correcta de montos. Genera un reporte simple en la consola y en un archivo
 * denominado {@code test-report.txt}.</p>
 */
public final class TestRunner {
    private TestRunner() {
    }

    public static void main(String[] args) throws IOException {
        /**
         * Ejecuta los casos de prueba definidos en esta clase.
         *
         * <p>Inicializa el conteo de pruebas pasadas y falladas, ejecuta cada
         * método de prueba mediante {@link #runTest(String, TestCase, StringBuilder)},
         * muestra un resumen en la consola y genera un archivo {@code test-report.txt}
         * con los resultados de cada prueba.</p>
         *
         * @param args no se utilizan en esta implementación, pero se admite para compatibilidad
         * @throws IOException si ocurre un error al escribir el reporte
         */
        int passedCount = 0;
        int failedCount = 0;
        StringBuilder reportBuilder = new StringBuilder();

        // Ejecutar cada prueba y capturar resultado
        if (runTest("testPathTraversal", TestRunner::testPathTraversal, reportBuilder)) {
            passedCount++;
        } else {
            failedCount++;
        }
        if (runTest("testCounts", TestRunner::testCounts, reportBuilder)) {
            passedCount++;
        } else {
            failedCount++;
        }
        if (runTest("testTotals", TestRunner::testTotals, reportBuilder)) {
            passedCount++;
        } else {
            failedCount++;
        }

        // Resumen
        System.out.println("Pruebas ejecutadas: " + (passedCount + failedCount));
        System.out.println("Pruebas pasadas: " + passedCount);
        System.out.println("Pruebas falladas: " + failedCount);

        // Escribir reporte a archivo
        Files.write(Paths.get("test-report.txt"), reportBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }

    @FunctionalInterface
    private interface TestCase {
        void run() throws Exception;
    }

    private static boolean runTest(String testName, TestCase testCase, StringBuilder reportBuilder) {
        try {
            testCase.run();
            reportBuilder.append(testName).append(" PASSED\n");
            return true;
        } catch (AssertionError | Exception exception) {
            reportBuilder.append(testName)
                          .append(" FAILED: ")
                          .append(exception.getMessage())
                          .append("\n");
            return false;
        }
    }

    // Prueba 1: Detectar path traversal e invalidez de rutas
    private static void testPathTraversal() throws Exception {
        Path temporaryDirectory = Files.createTempDirectory("importerBase");
        Importer importer = new Importer(temporaryDirectory.toString());
        // Crear archivo de prueba válido dentro del directorio base
        Path validFile = temporaryDirectory.resolve("dummy.csv");
        Files.write(validFile, "1,IN,10.00,MXN,desc".getBytes(StandardCharsets.UTF_8));
        // Ruta con ..
        boolean caughtException = false;
        try {
            importer.importCsvFile("../etc/passwd");
        } catch (SecurityException exception) {
            caughtException = true;
        }
        assertTrue(caughtException, "Se esperaba SecurityException para path traversal '..'");
        // Ruta absoluta
        caughtException = false;
        try {
            importer.importCsvFile("/etc/passwd");
        } catch (SecurityException exception) {
            caughtException = true;
        }
        assertTrue(caughtException, "Se esperaba SecurityException para ruta absoluta");
        // Ruta con caracter ':'
        caughtException = false;
        try {
            importer.importCsvFile("C:\\Windows\\System32");
        } catch (SecurityException exception) {
            caughtException = true;
        }
        assertTrue(caughtException, "Se esperaba SecurityException para ruta con ':'");
        // Ruta válida debería pasar sin excepción
        ImportResult importResult = importer.importCsvFile("dummy.csv");
        assertEquals(1, importResult.getValidTransactionCount(), "Debería procesar el archivo válido");
        assertEquals(0, importResult.getInvalidTransactionCount(), "No debería haber registros inválidos");
    }

    // Prueba 2: Conteo de registros válidos e inválidos
    private static void testCounts() throws Exception {
        Path temporaryDirectory = Files.createTempDirectory("importerCounts");
        String csvFileName = "transactions.csv";
        // Preparar CSV con 3 líneas válidas y 2 inválidas
        StringBuilder csvContent = new StringBuilder();
        csvContent.append("1,IN,100.00,MXN,Ingreso válido\n");
        csvContent.append("2,OUT,50.00,MXN,Egreso válido\n");
        csvContent.append("3,OUT,-10.00,MXN,Monto negativo\n"); // inválido
        csvContent.append("4,IN,abc,MXN,Monto no numérico\n"); // inválido
        csvContent.append("5,IN,25.00,MXN,Otro ingreso\n");
        Path csvFile = temporaryDirectory.resolve(csvFileName);
        Files.write(csvFile, csvContent.toString().getBytes(StandardCharsets.UTF_8));
        Importer importer = new Importer(temporaryDirectory.toString());
        ImportResult importResult = importer.importCsvFile(csvFileName);
        assertEquals(3, importResult.getValidTransactionCount(), "Debe haber 3 registros válidos");
        assertEquals(2, importResult.getInvalidTransactionCount(), "Debe haber 2 registros inválidos");
    }

    // Prueba 3: Totales correctos
    private static void testTotals() throws Exception {
        Path temporaryDirectory = Files.createTempDirectory("importerTotals");
        String csvFileName = "totals.csv";
        // Transacciones: dos IN y una OUT
        StringBuilder csvContent = new StringBuilder();
        csvContent.append("1,IN,10.50,MXN,Primera\n");
        csvContent.append("2,OUT,5.25,MXN,Segunda\n");
        csvContent.append("3,IN,4.75,MXN,Tercera\n");
        Path csvFile = temporaryDirectory.resolve(csvFileName);
        Files.write(csvFile, csvContent.toString().getBytes(StandardCharsets.UTF_8));
        Importer importer = new Importer(temporaryDirectory.toString());
        ImportResult importResult = importer.importCsvFile(csvFileName);
        assertEquals(3, importResult.getValidTransactionCount(), "Todas las transacciones son válidas");
        assertEquals(0, importResult.getInvalidTransactionCount(), "No debe haber transacciones inválidas");
        assertEquals(new java.math.BigDecimal("15.25"), importResult.getTotalIncome(), "Total IN incorrecto");
        assertEquals(new java.math.BigDecimal("5.25"), importResult.getTotalExpense(), "Total OUT incorrecto");
    }

    // Helpers de aserción
    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message + " (esperado=" + expected + ", actual=" + actual + ")");
        }
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(message + " (esperado=" + expected + ", actual=" + actual + ")");
        }
    }
}