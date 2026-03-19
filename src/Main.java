public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: java Main <archivo.csv>");
            return;
        }

        try {
            Importer importer = new Importer();
            ImportResult result = importer.processCsv(args[0]);
            result.printSummary();
        } catch (SecurityException e) {
            System.err.println("ALERTA DE SEGURIDAD: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " +
                    e.getMessage());
        }
    }
}