package test;
import service.Importer;
import dto.ImportResult;

public class TestRunner {

    public static void main(String[] args) {

        int passed = 0;
        int failed = 0;

        if (testValidProcessing()) passed++; else failed++;
        if (testInvalidFile()) passed++; else failed++;
        if (testPathTraversal()) passed++; else failed++;

        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
    }

    private static boolean testInvalidFile() {
        try {
            Importer importer = new Importer();
            importer.importFile("no-existe.csv");

            System.out.println("testInvalidFile FAILED: expected exception");
            return false;

        } catch (Exception e) {
            return true;
        }
    }

    private static boolean testPathTraversal() {
        try {
            Importer importer = new Importer();
            importer.importFile("../data.csv");

            System.out.println("testPathTraversal FAILED: expected security exception");
            return false;

        } catch (SecurityException e) {
            return true;
        } catch (Exception e) {
            System.out.println("testPathTraversal FAILED: wrong exception");
            return false;
        }
    }

    private static boolean testValidProcessing() {
        try {
            Importer importer = new Importer();
            ImportResult result = importer.importFile("data.csv");

            System.out.println("Result:\n" + result);

            return result.toString().contains("Valid: 3");

        } catch (Exception e) {
            System.out.println("testValidProcessing FAILED: " + e.getMessage());
            return false;
        }
    }
}