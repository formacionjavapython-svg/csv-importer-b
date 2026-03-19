public class Main {
    public static void main(String[] args) {

        System.out.println("Iniciando lectura de archivo CSV...");

        Importer importer = new Importer();
        importer.leerCSV("data.csv");

        System.out.println("Proceso terminado");
    }
}