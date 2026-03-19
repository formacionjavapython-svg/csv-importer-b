import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import main.java.Importer;

public class ImporterForTests extends Importer {
    private final String baseDir;

    public ImporterForTests(final String baseDirText) {
        if (baseDirText == null || baseDirText.isBlank()) {
            throw new IllegalArgumentException("La carpeta base no puede estar vacia");
        }
        this.baseDir = baseDirText;
    }

    @Override
    public Path validatePath(final String fileName) throws IOException {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("El nombre del archivo no puede estar vacio");
        }

        final Path basePath = Paths.get(baseDir).toAbsolutePath().normalize();
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
