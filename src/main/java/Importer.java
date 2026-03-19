package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Importer {
    private static final String BASE_DIR = "data";

    public ImportResult importCsv(final String fileName) {
        final ImportResult result = new ImportResult();

        try {
            final Path safePath = validatePath(fileName);
            readFile(safePath, result);
        } catch (Exception exception) {
            result.addError("Error general de importacion: " + exception.getMessage());
        }

        return result;
    }

    private void readFile(final Path filePath, final ImportResult result) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            int lineNumber = 0;
            boolean header = true;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (header) {
                    header = false;
                    continue;
                }
                processLine(line, lineNumber, result);
            }
        }
    }

    private void processLine(
            final String line,
            final int lineNumber,
            final ImportResult result) {
        try {
            final Transaction transaction = parseLine(line);
            result.addValidTransaction(transaction);
        } catch (Exception exception) {
            result.addError("Linea " + lineNumber + ": " + exception.getMessage());
        }
    }

    private Transaction parseLine(final String line) {
        final String[] parts = line.split(",");
        if (parts.length != 5) {
            throw new IllegalArgumentException("La linea no tiene 5 columnas");
        }

        final String id = parts[0].trim();
        final String typeText = parts[1].trim().toUpperCase();
        final String amount = parts[2].trim();
        final String currency = parts[3].trim();
        final String description = parts[4].trim();

        final TxType type = parseType(typeText);
        final Money money = new Money(amount, currency);

        return new Transaction(id, type, money, description);
    }

    private TxType parseType(final String typeText) {
        try {
            return TxType.valueOf(typeText);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Tipo invalido: " + typeText);
        }
    }

    public Path validatePath(final String fileName) throws IOException {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("El nombre del archivo no puede estar vacio");
        }

        final Path basePath = Paths.get(BASE_DIR).toAbsolutePath().normalize();
        final Path targetPath = basePath.resolve(fileName).normalize();

        if (!targetPath.startsWith(basePath)) {
            throw new SecurityException("Posible path traversal detectado");
        }
        if (!Files.exists(targetPath)) {
            throw new IOException("El archivo no existe: " + fileName);
        }

        return targetPath;
    }
}
