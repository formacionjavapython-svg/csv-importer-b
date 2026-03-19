import main.ImportResult;
import main.Importer;

public class Main {
    public static void main(String[] args) {
        // Mi Csv
        String csvFilePath = "src/test/datos.csv"; 

        Importer importer = new Importer();
        try {
            ImportResult result = importer.importCSV(csvFilePath);
            result.printSummary();
        } catch (SecurityException se) {
            System.err.println("Error de seguridad: " + se.getMessage());
        }
    }
}

