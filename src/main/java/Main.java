package main.java;

class Main {

    public static void main(String[] args) {
        try {
            // Obtener directorio base desde variable de entorno o usar "data" por defecto
            String baseDir = System.getenv("IMPORT_BASE_DIR");
            if (baseDir == null || baseDir.isBlank()) {
                baseDir = "data";
            }

            System.out.println("Using base directory: " + baseDir);

            Importer importer = new Importer(baseDir);
            ImportResult result = importer.importFile("transactions.csv");

            result.printSummary();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }







}