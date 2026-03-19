package com.escuela.csvimporter.importer;

import com.escuela.csvimporter.model.ImportResult;
import com.escuela.csvimporter.model.Transaction;
import com.escuela.csvimporter.parser.TransactionParser;
import com.escuela.csvimporter.security.PathSecurity;
import com.escuela.csvimporter.util.CsvReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Importer {

    private final PathSecurity pathSecurity;
    private final CsvReader csvReader;
    private final TransactionParser parser;

    public Importer() {
        this.pathSecurity = new PathSecurity();
        this.csvReader = new CsvReader();
        this.parser = new TransactionParser();
    }

    public ImportResult importFile(String fileName) {
        ImportResult result = new ImportResult();

        try {
            Path path = pathSecurity.resolveSecurePath(fileName);
            List<String> lines = csvReader.readAllLines(path);

            if (lines.isEmpty()) {
                return result;
            }

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);

                try {
                    Transaction transaction = parser.parse(line);
                    result.addValid(transaction);
                } catch (Exception e) {
                    result.addInvalid("Línea " + (i + 1) + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            result.addInvalid("Error de lectura: " + e.getMessage());
        } catch (Exception e) {
            result.addInvalid("Error general: " + e.getMessage());
        }

        return result;
    }
}