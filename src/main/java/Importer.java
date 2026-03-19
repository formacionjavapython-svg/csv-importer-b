package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Importer {
    private final Path basePath;

    public Importer(String baseDir) throws IOException {
        if (baseDir == null || baseDir.isBlank()) {
            throw new IllegalArgumentException("Base directory cannot be null");
        }

        this.basePath = Paths.get(baseDir).toRealPath();
    }

    private Path validatePath(String fileName) throws SecurityException {
        if (fileName == null || fileName.isBlank()) {
            throw new SecurityException("File name cannot be null or empty");
        }

        // Detectar patrones maliciosos
        if (fileName.contains("..") || fileName.contains("\\") ||
                fileName.contains(":")) {
            throw new SecurityException("Path traversal detected: " + fileName);
        }

        try {
            // Normalizar y resolver la ruta
            Path filePath = basePath.resolve(fileName).normalize();

            // Verificar que la ruta final esté dentro del directorio base
            if (!filePath.startsWith(basePath)) {
                throw new SecurityException("Path traversal detected: " + fileName);
            }

            return filePath;
        } catch (Exception e) {
            throw new SecurityException("Invalid path: " + fileName);
        }
    }

    public ImportResult importFile(String fileName) {
        List<String> errors = new ArrayList<>();
        int validCount = 0;
        int invalidCount = 0;
        Money totalIn = null;
        Money totalOut = null;

        try {
            Path filePath = validatePath(fileName);

            if (!Files.exists(filePath)) {
                errors.add("File not found: " + fileName);
                return new ImportResult(0, 0, null, null, errors);
            }

            try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                String line;
                boolean firstLine = true;
                int lineNumber = 0;

                while ((line = reader.readLine()) != null) {
                    lineNumber++;

                    // Saltar encabezado
                    if (firstLine) {
                        firstLine = false;
                        continue;
                    }

                    Transaction tx = Transaction.parseLine(line);

                    if (tx == null) {
                        invalidCount++;
                        errors.add("Line " + lineNumber + ": Invalid format");
                        continue;
                    }

                    validCount++;

                    // Acumular totales
                    if (tx.getType() == TxType.IN) {
                        totalIn = (totalIn == null) ?
                                tx.getMoney() : totalIn.add(tx.getMoney());
                    } else {
                        totalOut = (totalOut == null) ?
                                tx.getMoney() : totalOut.add(tx.getMoney());
                    }
                }
            }

        } catch (SecurityException e) {
            errors.add("Security error: " + e.getMessage());
        } catch (IOException e) {
            errors.add("IO error: " + e.getMessage());
        }

        return new ImportResult(validCount, invalidCount, totalIn, totalOut, errors);
    }
}