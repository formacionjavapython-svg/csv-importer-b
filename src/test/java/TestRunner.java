package test.java;

import main.java.Importer;
import main.java.ImportResult;
import java.math.BigDecimal;

public class TestRunner {

    public static void main(String[] args) {
        System.out.println("Iniciando TestRunner Personalizado...\n");
        int passed = 0;
        int failed = 0;

        try {
            System.out.print("Test 1: Prevención de Path Traversal... ");
            Importer importer = new Importer();
            importer.process("../../../../etc/passwd"); 
            System.out.println("FALLÓ (Debería haber lanzado SecurityException)");
            failed++;
        } catch (SecurityException e) {
            System.out.println("PASÓ");
            passed++;
        } catch (Exception e) {
            System.out.println("FALLÓ (Excepción incorrecta: " + e.getMessage() + ")");
            failed++;
        }

        try {
            System.out.print("Test 2 & 3: Procesamiento CSV válido e inválido... ");
            Importer importer = new Importer();
            ImportResult result = importer.process("datos.csv");

            boolean countsMatch = (result.getValidCount() == 15 && result.getInvalidCount() == 0);
            boolean totalsMatch = (result.getTotalIn().compareTo(new BigDecimal("4260")) == 0 && 
                                   result.getTotalOut().compareTo(new BigDecimal("1360")) == 0);

            if (countsMatch && totalsMatch) {
                System.out.println("PASÓ");
                passed++;
            } else {
                System.out.println("FALLÓ (Los contadores o totales no coinciden)");
                failed++;
            }
        } catch (Exception e) {
            System.out.println("FALLÓ (Error inesperado: " + e.getMessage() + ")");
            failed++;
        }

        System.out.println("\n--- RESULTADOS DEL TEST RUNNER ---");
        System.out.println("Pruebas ejecutadas: " + (passed + failed));
        System.out.println("Pasaron: " + passed);
        System.out.println("Fallaron: " + failed);

        if (failed > 0) {
            System.exit(1);
        }
    }
}