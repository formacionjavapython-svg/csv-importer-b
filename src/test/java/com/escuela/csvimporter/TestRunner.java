package com.escuela.csvimporter;

import com.escuela.csvimporter.importer.Importer;
import com.escuela.csvimporter.model.ImportResult;
import com.escuela.csvimporter.security.PathSecurity;

public class TestRunner {

    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;

        if (testPathTraversalBlocked()) {
            passed++;
        } else {
            failed++;
        }

        if (testImportCounts()) {
            passed++;
        } else {
            failed++;
        }

        if (testTotals()) {
            passed++;
        } else {
            failed++;
        }

        System.out.println("=== RESULTADO DE PRUEBAS ===");
        System.out.println("Tests aprobados: " + passed);
        System.out.println("Tests fallidos: " + failed);
    }

    private static boolean testPathTraversalBlocked() {
        try {
            PathSecurity security = new PathSecurity();
            security.resolveSecurePath("../etc/passwd");
            System.out.println("testPathTraversalBlocked: FAIL");
            return false;
        } catch (Exception e) {
            System.out.println("testPathTraversalBlocked: PASS");
            return true;
        }
    }

    private static boolean testImportCounts() {
        try {
            Importer importer = new Importer();
            ImportResult result = importer.importFile("transactions.csv");

            boolean ok = result.getValidCount() == 3
                    && result.getInvalidCount() == 3;

            System.out.println("testImportCounts: " + (ok ? "PASS" : "FAIL"));
            return ok;
        } catch (Exception e) {
            System.out.println("testImportCounts: FAIL");
            return false;
        }
    }

    private static boolean testTotals() {
        try {
            Importer importer = new Importer();
            ImportResult result = importer.importFile("transactions.csv");

            boolean ok = result.getTotalIn().toString().equals("1300.50")
                    && result.getTotalOut().toString().equals("250.00");

            System.out.println("testTotals: " + (ok ? "PASS" : "FAIL"));
            return ok;
        } catch (Exception e) {
            System.out.println("testTotals: FAIL");
            return false;
        }
    }
}