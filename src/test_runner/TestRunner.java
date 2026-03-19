package test_runner;

import main.java.ImportResult;
import main.java.Importer;

public class TestRunner {

    public static void main(String[] args) {

        TestRunner runner = new TestRunner();
        runner.ejecutar();

    }

    public void ejecutar() {

        probarPathTraversal();
        probarConteo();
        probarTotales();

    }

    private void probarPathTraversal() {

        System.out.println("Caso 1: Bloqueo de Path Traversal");

        try {

            Importer importer = new Importer();

            importer.importarArchivo("../etc/passwd");

            System.out.println("Falló: no se bloqueó el path traversal");

        } catch (SecurityException e) {

            System.out.println("Path traversal bloqueado correctamente");

        } catch (Exception e) {

            System.out.println("Error inesperado:");
            e.printStackTrace();

        }
    }

    private void probarConteo() {

        System.out.println("\nCaso 2: Conteo válidos / inválidos");

        try {

            Importer importer = new Importer();
            ImportResult resultado = importer.importarArchivo("test_data.csv");

            System.out.println("Registros válidos: " + resultado.validos);
            System.out.println("Registros inválidos: " + resultado.invalidos);

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    private void probarTotales() {

        System.out.println("\nCaso 3: Totales de ingresos y egresos");

        try {

            Importer importer = new Importer();
            ImportResult resultado = importer.importarArchivo("test_data.csv");

            System.out.println("Total ingresos: " + resultado.totalIngresos);
            System.out.println("Total egresos: " + resultado.totalEgresos);

            imprimirResumen(resultado);

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    private void imprimirResumen(ImportResult resultado) {

        System.out.println("\n===== RESUMEN DEL PROCESO =====");

        System.out.println("Contador válidos: " + resultado.validos);
        System.out.println("Contador inválidos: " + resultado.invalidos);

        System.out.println("Total ingresos: " + resultado.totalIngresos);
        System.out.println("Total egresos: " + resultado.totalEgresos);

        System.out.println("\nLista de errores:");

        if (resultado.errores.isEmpty()) {
            System.out.println("No se encontraron errores.");
        } else {
            for (String error : resultado.errores) {
                System.out.println("- " + error);
            }
        }
    }
}