package main.java;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;

public class Importer extends Money {

    public Importer(String cantidad, String moneda) {
        super(cantidad, moneda);
    }

    private final Path allowedDir;

    public Importer(String allowedDir) {
        this.allowedDir = Paths.get(allowedDir).toAbsolutePath().normalize();
    }

    public ImportResult importFile(String filePath) throws IOException {

        Path inputPath = Paths.get(filePath).toAbsolutePath().normalize();

        // Protección contra Path Traversal
        if (!inputPath.startsWith(allowedDir)) {
            throw new SecurityException("Access denied: invalid path");
        }

        ImportResult result = new ImportResult();

        try (Stream<String> lines = Files.lines(inputPath)) {

            lines.skip(1) // skip header
                    .forEach(line -> processLine(line, result));
        }

        return result;
    }

    private void processLine(String line, ImportResult result) {

        try {
            String[] parts = line.split(",");

            Transaction tx = Transaction.of(
                    parts[0],
                    parts[1],
                    parts[2],
                    parts[3],
                    parts.length > 4 ? parts[4] : "");

            result.addValid(tx);

        } catch (Exception e) {
            result.addInvalid("Line error: " + line + " -> " + e.getMessage());
        }
    }
}