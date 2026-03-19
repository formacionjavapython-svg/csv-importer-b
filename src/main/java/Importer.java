package main.java;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Importer {

    private static final String BASE_DIR = "C:\\Repos\\Java\\formacionjavapython\\csv-importer-b\\data\\";

    public ImportResult importCSV(String filePath) {
        System.out.println("==========================================");
        System.out.println("SOLICITUD: " + filePath);
        System.out.println("==========================================");

        ImportResult result = new ImportResult();

        // Validar path traversal
        if (filePath.contains("..") || filePath.contains("/") ||
                filePath.contains("\\") || filePath.contains(":")) {
            result.agregaError("ACCESO DENEGADO: Caracteres no permitidos");
            return result;
        }

        String rutaCompleta = BASE_DIR + filePath;
        System.out.println("Buscando en: " + rutaCompleta);

        File file = new File(rutaCompleta);

        if (!file.exists()) {
            result.agregaError("Archivo NO encontrado: " + filePath);
            return result;
        }

        System.out.println("Archivo encontrado. Leyendo contenido...");

        try {
            // Intentar leer el archivo
            List<String> lines = Files.readAllLines(Paths.get(rutaCompleta));
            System.out.println("Líneas leídas: " + lines.size());

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;

                if (i == 0 && line.toLowerCase().contains("id")) {
                    System.out.println("Encabezado: " + line);
                    continue;
                }

                processLine(line, i + 1, result);
            }

        } catch (IOException e) {
            // AHORA VEMOS EL ERROR REAL
            String errorDetallado = "Error leyendo archivo: " + e.getClass().getSimpleName() +
                    " - " + e.getMessage();
            System.out.println("Error: " + errorDetallado);
            result.agregaError(errorDetallado);

            // Mostrar más detalles
            e.printStackTrace(); // Esto muestra la traza completa del error
        }

        return result;
    }

    private void processLine(String line, int lineNumber, ImportResult result) {
        System.out.print("Linea " + lineNumber + ": " + line);

        String[] fields = line.split(",");

        if (fields.length != 4) {
            result.agregaError("Linea " + lineNumber + ": Se esperaban 4 campos, se encontraron " + fields.length);
            System.out.println(" Error");
            return;
        }

        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].trim();
        }

        String id = fields[0];
        String tipoStr = fields[1].toUpperCase();
        String moneda = fields[2].toUpperCase();
        String valorStr = fields[3];

        if (id.isEmpty()) {
            result.agregaError("Linea " + lineNumber + ": ID vacio");
            System.out.println(" Error");
            return;
        }

        TxType tipo;
        if (tipoStr.equals("IN")) {
            tipo = TxType.IN;
        } else if (tipoStr.equals("OUT")) {
            tipo = TxType.OUT;
        } else {
            result.agregaError("Linea " + lineNumber + ": Tipo invalido " + tipoStr + ". Debe ser IN o OUT");
            System.out.println(" Error");
            return;
        }

        if (moneda.isEmpty()) {
            result.agregaError("Linea " + lineNumber + ": Moneda vacia");
            System.out.println(" Error");
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr);
            if (valor < 0) {
                result.agregaError("Linea " + lineNumber + ": Valor negativo no permitido: " + valor);
                System.out.println(" Error");
                return;
            }
        } catch (NumberFormatException e) {
            result.agregaError("Linea " + lineNumber + ": Valor invalido " + valorStr + ". Debe ser un numero");
            System.out.println(" Error");
            return;
        }

        Transaction transaction = new Transaction(id, tipo, moneda, valor);

        result.agregaTrasacionValida(transaction);
        System.out.println(" Valida (" + tipo + " " + valor + " " + moneda + ")");
    }
}
