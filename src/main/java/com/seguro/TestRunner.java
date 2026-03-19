package com.seguro;

import com.seguro.model.ImportResult;
import com.seguro.service.Importer;

public class TestRunner {

    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;

        if (testPathTraversal()) {
            passed++;
        } else {
            failed++;
        }

        if (testValidInvalidCount()) {
            passed++;
        } else {
            failed++;
        }

        if (testTotals()) {
            passed++;
        } else {
            failed++;
        }

        System.out.println("\n=== REPORTE DE PRUEBAS ===");
        System.out.println("Pasadas: " + passed);
        System.out.println("Fallidas: " + failed);
    }

    private static boolean testPathTraversal() {
        try {
            Importer importer = new Importer();
            ImportResult result = importer.importFile("../secreto.csv");

            boolean ok = result.getInvalidCount() > 0;
            System.out.println("testPathTraversal: " + (ok ? "PASS" : "FAIL"));
            return ok;
        } catch (Exception e) {
            System.out.println("testPathTraversal: FAIL");
            return false;
        }
    }

    private static boolean testValidInvalidCount() {
        try {
            Importer importer = new Importer();
            ImportResult result = importer.importFile("transacciones_prueba.csv");

            boolean ok = result.getValidCount() == 3 && result.getInvalidCount() == 2;
            System.out.println("testValidInvalidCount: " + (ok ? "PASS" : "FAIL"));
            return ok;
        } catch (Exception e) {
            System.out.println("testValidInvalidCount: FAIL");
            return false;
        }
    }

    private static boolean testTotals() {
        try {
            Importer importer = new Importer();
            ImportResult result = importer.importFile("transacciones_prueba.csv");

            boolean ok = result.getTotalIn().toString().equals("300.50")
                    && result.getTotalOut().toString().equals("25.00");

            System.out.println("testTotals: " + (ok ? "PASS" : "FAIL"));
            return ok;
        } catch (Exception e) {
            System.out.println("testTotals: FAIL");
            return false;
        }
    }
}