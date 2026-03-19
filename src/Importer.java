import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Importer {

    public ImportResult processCsv(String fileName) throws Exception {
        // Validación de seguridad contra Path Traversal
        String baseDirStr = System.getenv("IMPORT_BASE_DIR");
        if (baseDirStr == null) {
            throw new IllegalStateException("La variable de entorno IMPORT_BASE_DIR no está configurada.");
        }

        Path basePath = Paths.get(baseDirStr).toRealPath();
        Path filePath = basePath.resolve(fileName).normalize();

        if (!filePath.startsWith(basePath)) {
            throw new SecurityException("Path traversal detected");
        }

        ImportResult result = new ImportResult();

        // Pipeline funcional 
        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) { isHeader = false; continue; } // Salta encabezados

                try {
                    // Parseo de columnas
                    String[] parts = line.split(",");
                    if (parts.length != 5) {
                        throw new IllegalArgumentException("Formato de columnas incorrecto");
                    }

                    // Validacion
                    Transaction tx = new Transaction(parts[0], parts[1], parts[2], parts[3], parts[4]);

                    // Acumulacion de resultados
                    result.addValid(tx);
                } catch (Exception ex) {
                    result.addInvalid("Error en línea [" + line + "]: " + ex.getMessage());
                }
            }
        }
        return result;
    }
}