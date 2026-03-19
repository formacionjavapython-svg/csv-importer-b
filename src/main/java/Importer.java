package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;



public class Importer {
    private final Path basePath;

    public Importer(Path basePath) {
        this.basePath = basePath.normalize().toAbsolutePath();
    }

  
    public ImportResult importFile(String fileName) throws IOException {
        Path filePath = basePath.resolve(fileName).normalize();

        if (!filePath.startsWith(basePath)) {
            throw new SecurityException("Acceso denegado: la ruta está fuera del directorio permitido");
        }
        if (!Files.isRegularFile(filePath) || !Files.isReadable(filePath)) {
            throw new IOException("El archivo no existe o no se puede leer: " + filePath);
        }

        ImportResult.Builder builder = ImportResult.builder();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }
                processLine(line, lineNumber, builder);
            }
        }
        return builder.build();
    }

    private void processLine(String line, int lineNumber, ImportResult.Builder builder) {
        String[] fields = line.split(",", -1);
        if (fields.length < 5) {
            builder.addInvalid("Línea " + lineNumber + ": número insuficiente de campos (" + fields.length + ")");
            return;
        }

        String id = fields[0].trim();
        String typeStr = fields[1].trim();
        String amountStr = fields[2].trim();
        String currency = fields[3].trim();
        String description = fields.length > 4 ? fields[4].trim() : "";

    
        TxType type;
        try {
            type = TxType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            builder.addInvalid("Línea " + lineNumber + ": tipo inválido '" + typeStr + "'");
            return;
        }

   
        Money money;
        try {
            money = new Money(amountStr, currency);
        } catch (IllegalArgumentException e) {
            builder.addInvalid("Línea " + lineNumber + ": monto/moneda inválido - " + e.getMessage());
            return;
        }

      
        try {
            Transaction tx = new Transaction(id, type, money, description);
            builder.addValid(tx);
        } catch (IllegalArgumentException e) {
            builder.addInvalid("Línea " + lineNumber + ": datos de transacción inválidos - " + e.getMessage());
        }
    }
}