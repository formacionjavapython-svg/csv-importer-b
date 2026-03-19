public class Main {
    public static void main(final String[] args) {
        final Importer importer = new Importer();
        final ImportResult result = importer.importCsv("transactions.csv");

        printSummary(result);
        printErrors(result);
    }

    private static void printSummary(final ImportResult result) {
        System.out.println("=== RESUMEN DE IMPORTACION ===");
        System.out.println("Registros validos: " + result.getValidCount());
        System.out.println("Registros invalidos: " + result.getInvalidCount());
        System.out.println("Total ingresos: " + result.getTotalIn());
        System.out.println("Total egresos: " + result.getTotalOut());
    }

    private static void printErrors(final ImportResult result) {
        System.out.println("=== ERRORES ===");
        if (result.getErrors().isEmpty()) {
            System.out.println("Sin errores");
            return;
        }

        for (String error : result.getErrors()) {
            System.out.println(error);
        }
    }
}