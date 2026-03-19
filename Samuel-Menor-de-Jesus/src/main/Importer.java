package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Importer {
    private ImportResult result;

    public Importer() {
        this.result = new ImportResult();
    }

    public ImportResult importCSV(String csvFilePath) {
        validatePath(csvFilePath);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void validatePath(String csvFilePath) {
        // Definir el directorio base permitido como un objeto Path absoluto y normalizado
        Path allowedBaseDir = Paths.get("src/test").toAbsolutePath().normalize();
        
        // Convertir la ruta de entrada a Path, hacerla absoluta y resolver referencias (el ../)
        Path resolvedPath = Paths.get(csvFilePath).toAbsolutePath().normalize();
        
        // Comparar usando el método startsWith de Path (NO de String). 
        // Esto maneja correctamente los separadores '/' o '\' según el SO.
        if (!resolvedPath.startsWith(allowedBaseDir)) {
            throw new SecurityException("Alerta de seguridad: Intento de acceso fuera del directorio permitido. Ruta: " + csvFilePath);
        }
    }

    private void processLine(String line) {
        String[] fields = line.split(",");
        if (fields.length != 5) {
            result.addInvalidTransaction(line);
            return;
        }

        String id = fields[0].trim();
        String type = fields[1].trim();
        String amountStr = fields[2].trim();
        String currency = fields[3].trim();
        String description = fields[4].trim();

        try {
            Money money = new Money(amountStr, currency);
            Transaction transaction = new Transaction(id, type, money, description);
            result.addValidTransaction(transaction);
        } catch (IllegalArgumentException e) {
            result.addInvalidTransaction(line + " : " + e.getMessage());
        }
    }
}
