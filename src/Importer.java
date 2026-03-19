import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Importer {

    // El metodo processCsv ahora incluye validación de seguridad para prevenir path traversal,
    public ImportResult processCsv(String fileName) throws Exception {
        Path filePath = validateSecurity(fileName);
        ImportResult result = new ImportResult();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) { isHeader = false; continue; }
                processLine(line, result);
            }
        }
        return result;
    }

    // Validación de seguridad para prevenir path traversal.
    private Path validateSecurity(String fileName) throws Exception {
        String baseDirStr = System.getenv("IMPORT_BASE_DIR");
        if (baseDirStr == null) {
            throw new IllegalStateException("La variable IMPORT_BASE_DIR no está configurada.");
        }
        Path basePath = Paths.get(baseDirStr).toRealPath();
        Path filePath = basePath.resolve(fileName).normalize();
        if (!filePath.startsWith(basePath)) {
            throw new SecurityException("Path traversal detected");
        }
        return filePath;
    }
// El mtodo processLine ahora maneja excepciones de manera más robusta, capturando cualquier error.
    private void processLine(String line, ImportResult result) {
        try {
            String[] parts = line.split(",");
            if (parts.length != 5) {
                throw new IllegalArgumentException("Formato de columnas incorrecto");
            }
            Transaction tx = new Transaction(parts[0], parts[1], parts[2], parts[3], parts[4]);
            result.addValid(tx);
        } catch (Exception ex) {
            result.addInvalid("Error en línea [" + line + "]: " + ex.getMessage());
        }
    }
}