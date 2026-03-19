package com.axity.csvimporter.test;

import com.axity.csvimporter.importer.Importer;
import com.axity.csvimporter.result.ImportResult;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    private final List<TestResult> results = new ArrayList<>();
    private int passed = 0;
    private int failed = 0;

    // Clase interna para almacenar resultados
    private static class TestResult {
        String name;
        boolean success;
        String message;
        long durationMs;

        TestResult(String name, boolean success, String message, long durationMs) {
            this.name = name;
            this.success = success;
            this.message = message;
            this.durationMs = durationMs;
        }
    }

    public void runTest(String testName, TestCase testCase) {
        System.out.print("🔷 Ejecutando: " + testName + "... ");

        long startTime = System.currentTimeMillis();
        try {
            testCase.run();
            long duration = System.currentTimeMillis() - startTime;

            results.add(new TestResult(testName, true, "OK", duration));
            passed++;
            System.out.println("✅ PASÓ (" + duration + "ms)");

        } catch (AssertionError e) {
            long duration = System.currentTimeMillis() - startTime;
            results.add(new TestResult(testName, false, "FALLÓ: " + e.getMessage(), duration));
            failed++;
            System.out.println("❌ FALLÓ (" + duration + "ms)");
            System.out.println("   └─ " + e.getMessage());

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            results.add(new TestResult(testName, false, "ERROR: " + e.getClass().getSimpleName() + " - " + e.getMessage(), duration));
            failed++;
            System.out.println("💥 ERROR (" + duration + "ms)");
            System.out.println("   └─ " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    // Método auxiliar para aserciones
    public static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null && actual == null) return;
        if (expected != null && expected.equals(actual)) return;
        if (actual != null && actual.equals(expected)) return;

        throw new AssertionError(message + " - Esperado: " + expected + ", Actual: " + actual);
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertThrows(Class<? extends Exception> expectedException, Runnable code, String message) {
        try {
            code.run();
            throw new AssertionError(message + " - Se esperaba excepción " + expectedException.getSimpleName() + " pero no se lanzó");
        } catch (Exception e) {
            if (!expectedException.isInstance(e)) {
                throw new AssertionError(message + " - Se esperaba " + expectedException.getSimpleName() +
                        " pero se lanzó " + e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }

    public void generateReport() {
        // Reporte en consola
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 REPORTE FINAL DE PRUEBAS");
        System.out.println("=".repeat(60));
        System.out.println("Ejecutado: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        System.out.println("-".repeat(60));

        for (TestResult result : results) {
            String icon = result.success ? "✅" : "❌";
            System.out.printf("%s %-50s %5dms\n", icon, result.name, result.durationMs);
            if (!result.success) {
                System.out.println("   └─ " + result.message);
            }
        }

        System.out.println("-".repeat(60));
        System.out.printf("Total: %d | ✅ Pasaron: %d | ❌ Fallaron: %d | ✅ Tasa de éxito: %.1f%%\n",
                results.size(), passed, failed, (passed * 100.0 / results.size()));
        System.out.println("=".repeat(60));

        // Guardar reporte a archivo
        saveReportToFile();
    }

    private void saveReportToFile() {
        try {
            Path reportDir = Paths.get("test-reports");
            if (!Files.exists(reportDir)) {
                Files.createDirectories(reportDir);
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
            Path reportFile = reportDir.resolve("test-report-" + timestamp + ".txt");

            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(reportFile))) {
                writer.println("=".repeat(60));
                writer.println("TEST REPORT - CSV Importer");
                writer.println("=".repeat(60));
                writer.println("Fecha: " + LocalDateTime.now());
                writer.println();

                for (TestResult result : results) {
                    writer.printf("%s %s (%dms)\n",
                            result.success ? "[PASS]" : "[FAIL]",
                            result.name,
                            result.durationMs);
                    if (!result.success) {
                        writer.println("  " + result.message);
                    }
                }

                writer.println("-".repeat(60));
                writer.printf("Total: %d | Pasaron: %d | Fallaron: %d\n",
                        results.size(), passed, failed);
                writer.println("=".repeat(60));
            }

            System.out.println("\n📁 Reporte guardado en: " + reportFile.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("⚠️ No se pudo guardar el reporte: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("🧪 === TEST RUNNER: CSV IMPORTER === 🧪\n");

        TestRunner runner = new TestRunner();
        Importer importer = new Importer();

        // =========================================================
        // PRUEBA 1: Path Traversal
        // =========================================================
        runner.runTest("Path Traversal - Bloqueo de ../etc/passwd", () -> {
            ImportResult result = importer.processFile("../etc/passwd");
            TestRunner.assertTrue(result.getInvalidCount() > 0,
                    "Debería registrar al menos 1 error por seguridad");
            TestRunner.assertTrue(result.getErrors().stream()
                            .anyMatch(e -> e.contains("path traversal") || e.contains("seguridad")),
                    "El mensaje de error debe indicar path traversal");
        });

        // =========================================================
        // PRUEBA 2: Archivo válido - Conteo de registros
        // =========================================================
        runner.runTest("Archivo válido - Conteo de transacciones", () -> {
            ImportResult result = importer.processFile("transacciones.csv");
            TestRunner.assertEquals(5, result.getValidCount(),
                    "Debe haber 5 transacciones válidas");
            TestRunner.assertEquals(0, result.getInvalidCount(),
                    "Debe haber 0 transacciones inválidas");
        });

        // =========================================================
        // PRUEBA 3: Archivo válido - Totales por moneda
        // =========================================================
        runner.runTest("Archivo válido - Totales IN por moneda", () -> {
            ImportResult result = importer.processFile("transacciones.csv");

            var inByCurrency = result.getTotalInByCurrency();
            TestRunner.assertEquals(3, inByCurrency.size(),
                    "Debe haber 3 monedas diferentes en IN");

            TestRunner.assertTrue(inByCurrency.containsKey("MXN"),
                    "Debe incluir MXN en ingresos");
            TestRunner.assertTrue(inByCurrency.containsKey("EUR"),
                    "Debe incluir EUR en ingresos");
            TestRunner.assertTrue(inByCurrency.containsKey("USD"),
                    "Debe incluir USD en ingresos");

            // Verificar montos específicos
            TestRunner.assertEquals("100.00", inByCurrency.get("MXN").getAmount().toString(),
                    "Total IN MXN debe ser 100.00");
            TestRunner.assertEquals("150.50", inByCurrency.get("EUR").getAmount().toString(),
                    "Total IN EUR debe ser 150.50");
            TestRunner.assertEquals("300.00", inByCurrency.get("USD").getAmount().toString(),
                    "Total IN USD debe ser 300.00");
        });

        // =========================================================
        // PRUEBA 4: Archivo válido - Totales OUT por moneda
        // =========================================================
        runner.runTest("Archivo válido - Totales OUT por moneda", () -> {
            ImportResult result = importer.processFile("transacciones.csv");

            var outByCurrency = result.getTotalOutByCurrency();
            TestRunner.assertEquals(2, outByCurrency.size(),
                    "Debe haber 2 monedas diferentes en OUT");

            TestRunner.assertEquals("75.25", outByCurrency.get("MXN").getAmount().toString(),
                    "Total OUT MXN debe ser 75.25");
            TestRunner.assertEquals("200.00", outByCurrency.get("USD").getAmount().toString(),
                    "Total OUT USD debe ser 200.00");
        });

        // =========================================================
        // PRUEBA 5: Archivo con errores - Conteo
        // =========================================================
        runner.runTest("Archivo con errores - Conteo válidos/inválidos", () -> {
            ImportResult result = importer.processFile("errores.csv");
            TestRunner.assertEquals(1, result.getValidCount(),
                    "Debe haber 1 transacción válida");
            TestRunner.assertEquals(4, result.getInvalidCount(),
                    "Debe haber 4 transacciones inválidas");
        });

        // =========================================================
        // PRUEBA 6: Archivo con errores - Mensajes específicos
        // =========================================================
        runner.runTest("Archivo con errores - Validación de mensajes", () -> {
            ImportResult result = importer.processFile("errores.csv");

            List<String> errors = result.getErrors();
            TestRunner.assertEquals(4, errors.size(),
                    "Debe haber 4 mensajes de error");

            // Verificar que cada error contiene palabras clave
            boolean hasNegativeError = errors.stream().anyMatch(e ->
                    e.contains("positivo") && e.contains("-200"));
            boolean hasTypeError = errors.stream().anyMatch(e ->
                    e.contains("Tipo") && e.contains("INVALido"));
            boolean hasZeroError = errors.stream().anyMatch(e ->
                    e.contains("positivo") && e.contains("0"));
            boolean hasFormatError = errors.stream().anyMatch(e ->
                    e.contains("Formato") && e.contains("abc"));

            TestRunner.assertTrue(hasNegativeError,
                    "Debe detectar monto negativo");
            TestRunner.assertTrue(hasTypeError,
                    "Debe detectar tipo inválido");
            TestRunner.assertTrue(hasZeroError,
                    "Debe detectar monto cero");
            TestRunner.assertTrue(hasFormatError,
                    "Debe detectar formato inválido");
        });

        // =========================================================
        // PRUEBA 7: Archivo inexistente
        // =========================================================
        runner.runTest("Archivo inexistente - Manejo de error", () -> {
            ImportResult result = importer.processFile("no-existe.csv");
            TestRunner.assertEquals(0, result.getValidCount(),
                    "No debe haber válidos");
            TestRunner.assertTrue(result.getInvalidCount() > 0,
                    "Debe haber al menos 1 error");
            TestRunner.assertTrue(result.getErrors().stream()
                            .anyMatch(e -> e.contains("no existe")),
                    "El mensaje debe indicar que el archivo no existe");
        });

        // =========================================================
        // PRUEBA 8: Headers inválidos (debe funcionar igual)
        // =========================================================
        runner.runTest("Header inválido - Tolerancia", () -> {
            // Crear archivo temporal con header diferente
            Path tempFile = Path.of("data/header-invalido.csv");
            String content = "ID,TIPO,MONTO,CURRENCY\n1,IN,100,MXN";
            Files.writeString(tempFile, content);

            try {
                ImportResult result = importer.processFile("header-invalido.csv");
                TestRunner.assertEquals(1, result.getValidCount(),
                        "Debe procesar aunque el header sea diferente");
            } finally {
                Files.deleteIfExists(tempFile);
            }
        });

        // Generar reporte final
        runner.generateReport();
    }
}
