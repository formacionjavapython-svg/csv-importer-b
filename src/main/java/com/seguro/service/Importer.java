package com.seguro.service;

import com.seguro.model.ImportResult;
import com.seguro.model.Money;
import com.seguro.model.Transaction;
import com.seguro.model.TxType;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Importer {

    public ImportResult importFile(String fileName) {
        ImportResult result = new ImportResult();

        try {
            Path safePath = resolveSafePath(fileName);

            try (BufferedReader reader = Files.newBufferedReader(safePath)) {
                String line;
                int lineNumber = 1;

                while ((line = reader.readLine()) != null) {
                    processLine(line, lineNumber, result);
                    lineNumber++;
                }
            }
        } catch (SecurityException e) {
            result.addInvalid("Ruta rechazada: " + e.getMessage());
        } catch (IOException e) {
            result.addInvalid("Error al leer archivo: " + e.getMessage());
        }

        return result;
    }

    private void processLine(String line, int lineNumber, ImportResult result) {
        try {
            Transaction transaction = parseAndValidate(line);
            result.addValid(transaction);
        } catch (IllegalArgumentException e) {
            result.addInvalid("Línea " + lineNumber + ": " + e.getMessage());
        }
    }

    private Transaction parseAndValidate(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("línea vacía");
        }

        String[] parts = line.split(",", -1);

        if (parts.length != 5) {
            throw new IllegalArgumentException("se esperaban 5 columnas: id,type,amount,currency,description");
        }

        String id = parts[0].trim();
        String typeText = parts[1].trim().toUpperCase();
        String amountText = parts[2].trim();
        String currency = parts[3].trim();
        String description = parts[4].trim();

        TxType type;
        try {
            type = TxType.valueOf(typeText);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("tipo inválido: " + typeText);
        }

        Money money = new Money(amountText, currency);

        if (money.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("monto negativo no permitido");
        }

        return new Transaction(id, type, money, description);
    }

    private Path resolveSafePath(String fileName) throws IOException {
        String baseDir = System.getenv("IMPORT_BASE_DIR");

        if (baseDir == null || baseDir.isBlank()) {
            throw new SecurityException("IMPORT_BASE_DIR no está definida");
        }

        if (fileName == null || fileName.isBlank()) {
            throw new SecurityException("nombre de archivo vacío");
        }

        if (fileName.contains("..") || fileName.contains(":")
                || fileName.startsWith("/") || fileName.startsWith("\\")) {
            throw new SecurityException("posible path traversal detectado");
        }

        Path basePath = Paths.get(baseDir).toRealPath();
        Path filePath = basePath.resolve(fileName).normalize().toRealPath();

        if (!filePath.startsWith(basePath)) {
            throw new SecurityException("archivo fuera del directorio permitido");
        }

        return filePath;
    }
}