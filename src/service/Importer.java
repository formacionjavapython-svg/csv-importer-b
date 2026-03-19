package service;

import dto.ImportResult;
import main.java.Transaction;
import main.java.TxType;
import main.java.Money;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

public class Importer {

    public ImportResult importFile(String filePath) {

        ImportResult result = new ImportResult();

        Path safePath = validatePath(filePath);

        try ( BufferedReader reader = new BufferedReader(
                new FileReader(safePath.toFile()) ); ) {

            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    Transaction tx = parseLine(line);

                    result.addValid();

                    if (tx.getType() == TxType.IN) {
                        result.addToIn(tx.getAmount().getAmount());
                    } else {
                        result.addToOut(tx.getAmount().getAmount());
                    }

                } catch (Exception e) {
                    result.addInvalid("Line: " + line + " Error: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }

        return result;
    }

    private Transaction parseLine(String line) {

        String[] parts = line.split(",");

        if (parts.length < 5) {
            throw new IllegalArgumentException("Invalid CSV format");
        }

        String id = parts[0];
        TxType type = TxType.valueOf(parts[1]);
        String amount = parts[2];
        String currency = parts[3];
        String description = parts[4];

        Money money = new Money(amount, currency);

        return new Transaction(id, type, money, description);
    }


    private Path validatePath(String fileName) {

        String baseDir = System.getenv("IMPORT_BASE_DIR");

        if (baseDir == null || baseDir.isBlank()) {
            throw new IllegalStateException("IMPORT_BASE_DIR not set");
        }

        try {
            Path basePath = Paths.get(baseDir).toRealPath();
            Path filePath = basePath.resolve(fileName).normalize();

            if (!filePath.startsWith(basePath)) {
                throw new SecurityException("Path traversal detected");
            }

            return filePath;

        } catch (IOException e) {
            throw new RuntimeException("Error resolving path", e);
        }
    }
}