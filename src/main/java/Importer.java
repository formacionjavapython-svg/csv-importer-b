import java.io.*;
import java.util.*;

public class Importer {
    private final String allowedDir = "data/";

    public ImportResult importFromCSV(String filePath) {
        ImportResult result = new ImportResult();

        // Validación de path traversal
        if (!filePath.startsWith(allowedDir)) {
            result.addInvalid("Ruta no permitida: " + filePath);
            return result;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    if (parts.length < 4) {
                        throw new IllegalArgumentException("Formato inválido: " + line);
                    }
                    String id = parts[0];
                    String type = parts[1];
                    Money money = new Money(parts[2], parts[3]);
                    String description = (parts.length > 4) ? parts[4] : "";
                    Transaction t = new Transaction(id, type, money, description);
                    result.addValid(t);
                } catch (Exception e) {
                    result.addInvalid(e.getMessage());
                }
            }
        } catch (IOException e) {
            result.addInvalid("Error leyendo archivo: " + e.getMessage());
        }

        return result;
    }
}