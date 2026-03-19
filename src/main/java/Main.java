public class Main {
    public static void main(String[] args) {

        System.out.println("Sistema de importación CSV - Cecilia Lopez");
        System.out.println("Procesando archivo...");

        Importer importer = new Importer();
        importer.leerCSV("data.csv");

        System.out.println("Proceso terminado");
    }
}