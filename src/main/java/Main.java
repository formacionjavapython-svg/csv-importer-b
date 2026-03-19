public class Main {
    public static void main(String[] args) {
        Importer importer = new Importer();
        ImportResult result = importer.importFromCSV("data/ejemplo.csv");
        result.printSummary();
    }
}