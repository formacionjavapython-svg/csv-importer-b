package test.java.data.test.java;

import main.java.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

public class TestRunner {
    private int totalTests = 0;
    private int passedTests = 0;
    private int failedTests = 0;
    private StringBuilder report = new StringBuilder();

    public void runAllTests() {
        System.out.println("=== Starting Tests ===\n");

        testPathTraversalBlocked();
        testValidInvalidCount();
        testTotalsCorrect();
        testMoneyValidation();
        testTransactionParsing();

        printResults();
        saveReport();
    }

    private void testPathTraversalBlocked() {
        runTest("Path Traversal Blocked", () -> {
            try {
                Importer importer = new Importer("data");

                // Intentar path traversal
                try {
                    importer.importFile("../etc/passwd");
                    throw new AssertionError("Should have thrown SecurityException");
                } catch (SecurityException e) {
                    // Esperado
                }

                try {
                    importer.importFile("..\\..\\Windows\\System32\\config");
                    throw new AssertionError("Should have thrown SecurityException");
                } catch (SecurityException e) {
                    // Esperado
                }

            } catch (Exception e) {
                throw new RuntimeException("Test failed: " + e.getMessage());
            }
        });
    }

    private void testValidInvalidCount() {
        runTest("Valid/Invalid Count", () -> {
            try {
                Importer importer = new Importer("data");
                ImportResult result = importer.importFile("transactions.csv");

                assertEqual(4, result.getValidCount(), "valid count");
                assertEqual(1, result.getInvalidCount(), "invalid count");

            } catch (Exception e) {
                throw new RuntimeException("Test failed: " + e.getMessage());
            }
        });
    }

    private void testTotalsCorrect() {
        runTest("Totals Correct", () -> {
            try {
                Importer importer = new Importer("data");
                ImportResult result = importer.importFile("transactions.csv");

                BigDecimal expectedIn = new BigDecimal("4751.25");
                BigDecimal expectedOut = new BigDecimal("800.00");

                assertNotNull(result.getTotalIn(), "totalIn");
                assertNotNull(result.getTotalOut(), "totalOut");

                assertEqual(expectedIn, result.getTotalIn().getAmount(), "total IN");
                assertEqual(expectedOut, result.getTotalOut().getAmount(), "total OUT");

            } catch (Exception e) {
                throw new RuntimeException("Test failed: " + e.getMessage());
            }
        });
    }

    private void testMoneyValidation() {
        runTest("Money Validation", () -> {
            // Test null amount
            Money money = Money.parse(null, "MXN");
            assertNull(money, "null amount should return null");

            // Test invalid amount
            money = Money.parse("invalid", "MXN");
            assertNull(money, "invalid amount should return null");

            // Test valid amount
            money = Money.parse("100.50", "MXN");
            assertNotNull(money, "valid amount should not be null");
            assertEqual(new BigDecimal("100.50"), money.getAmount(), "amount value");
        });
    }

    private void testTransactionParsing() {
        runTest("Transaction Parsing", () -> {
            // Test valid line
            String validLine = "TX001,IN,1500.50,MXN,Test transaction";
            Transaction tx = Transaction.parseLine(validLine);
            assertNotNull(tx, "valid transaction");
            assertEqual("TX001", tx.getId(), "transaction ID");
            assertEqual(TxType.IN, tx.getType(), "transaction type");

            // Test invalid line
            String invalidLine = "TX002,OUT,invalid,MXN,Bad transaction";
            tx = Transaction.parseLine(invalidLine);
            assertNull(tx, "invalid transaction should be null");
        });
    }

    // Métodos de utilidad

    private void runTest(String testName, Runnable test) {
        totalTests++;
        try {
            test.run();
            passedTests++;
            String msg = "✓ PASS: " + testName;
            System.out.println(msg);
            report.append(msg).append("\n");
        } catch (Exception e) {
            failedTests++;
            String msg = "✗ FAIL: " + testName + " - " + e.getMessage();
            System.out.println(msg);
            report.append(msg).append("\n");
        }
    }

    private void assertEqual(Object expected, Object actual, String message) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || !expected.equals(actual)) {
            throw new AssertionError(message + ": expected " + expected +
                    " but got " + actual);
        }
    }

    private void assertNotNull(Object obj, String message) {
        if (obj == null) {
            throw new AssertionError(message + " should not be null");
        }
    }

    private void assertNull(Object obj, String message) {
        if (obj != null) {
            throw new AssertionError(message + " should be null");
        }
    }

    private void printResults() {
        System.out.println("\n=== Test Results ===");
        System.out.println("Total: " + totalTests);
        System.out.println("Passed: " + passedTests);
        System.out.println("Failed: " + failedTests);
        System.out.println("Success Rate: " +
                (totalTests > 0 ? (passedTests * 100 / totalTests) : 0) + "%");
    }

    private void saveReport() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("test-report.txt"))) {
            writer.println("=== Test Report ===");
            writer.println("Date: " + java.time.LocalDateTime.now());
            writer.println();
            writer.print(report.toString());
            writer.println();
            writer.println("Total: " + totalTests);
            writer.println("Passed: " + passedTests);
            writer.println("Failed: " + failedTests);
            System.out.println("\nReport saved to test-report.txt");
        } catch (IOException e) {
            System.err.println("Error saving report: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        runner.runAllTests();
    }
}
