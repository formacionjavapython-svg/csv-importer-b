public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Por favor, proporciona el nombre del archivo CSV como argumento.");
            return;
        }

        Importer importer = new Importer();
        try {
            ImportResult result = importer.processCsv(args[0]);
            result.printSummary();
        } catch (SecurityException e) {
            System.err.println("ALERTA DE SEGURIDAD: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}