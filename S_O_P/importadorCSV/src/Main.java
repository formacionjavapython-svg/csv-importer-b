import importer.ImportResult;
import importer.Importer;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java Importer <archivo.csv>");
            return;
        }

        try {
            Importer importer = new Importer();
            ImportResult resultado = importer.importar(args[0]);
            resultado.generarReporte();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

    }
}