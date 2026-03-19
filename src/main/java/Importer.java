package main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Importer {

    public ImportResult process(String fileName) throws IOException {
        String baseDir = System.getenv("IMPORT_BASE_DIR");
        if (baseDir == null || baseDir.isBlank()) {
            throw new IllegalStateException("La variable de entorno IMPORT_BASE_DIR no está configurada");
        }

        Path basePath = Paths.get(baseDir).toRealPath();
        Path filePath = basePath.resolve(fileName).normalize();

        if (!filePath.startsWith(basePath)) {
            throw new SecurityException("Path traversal detected");
        }

        ImportResult result = new ImportResult();

        try (Stream<String> lines = Files.lines(filePath)) {
            lines.skip(1)
                 .forEach(line -> processLine(line, result));
        }

        return result;
    }


    private void processLine(String line, ImportResult result) {
        if (line == null || line.isBlank()) {
            return;
        }

        try {
            Transaction tx = parseAndValidate(line);
            result.addValid(tx);
        } catch (Exception e) {
            result.addInvalid("Error en línea [" + line + "]: " + e.getMessage());
        }
    }

    private Transaction parseAndValidate(String line) {
        String[] parts = line.split(",");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Formato incorrecto, se esperaban 5 columnas");
        }

        String id = parts[0].trim();
        String typeStr = parts[1].trim();
        String currencyStr = parts[2].trim();
        String amountStr = parts[3].trim();
        String description = "Sin descripción";

        Transaction.TxType type;
        try {
            type = Transaction.TxType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de transacción inválido: " + typeStr);
        }

        Money money = new Money(amountStr, currencyStr);

        return new Transaction(id, type, money, description);
    }
}