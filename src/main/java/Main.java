package main.java;

public class Main {
    public static void main(String[] args) {

        final Importer importer = new Importer();
        final ImportResult result = importer.importCsv("transactions.csv");

        System.out.println("=== RESUMEN DE IMPORTACION ===");
        System.out.println("Validos: " + result.getValidCount());
        System.out.println("Invalidos: " + result.getInvalidCount());
        System.out.println("Total IN: " + result.getTotalIn());
        System.out.println("Total OUT: " + result.getTotalOut());

        System.out.println("=== ERRORES ===");
        for (String error : result.getErrors()) {
            System.out.println(error);
        }
    }
}
