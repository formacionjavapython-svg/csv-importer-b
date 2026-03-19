package axel_hernandez;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileValidator {
    public static Path getSafePath(String fileName) throws Exception {
        // 1. Definimos el directorio base permitido (puedes usar una variable de entorno o carpeta fija)
        String baseDir = "data"; 
        
        // 2. Obtenemos la ruta real (toRealPath) para resolver enlaces simbólicos
        Path basePath = Paths.get(baseDir).toAbsolutePath().normalize();
        
        // 3. Resolvemos el nombre del archivo y lo normalizamos (quita los ../)
        Path filePath = basePath.resolve(fileName).normalize();

        // 4. Verificación CRÍTICA: ¿La ruta final sigue empezando con nuestra base?
        if (!filePath.startsWith(basePath)) {
            throw new SecurityException("¡Intento de Path Traversal detectado! No puedes salir de: " + baseDir);
        }

        return filePath;
    }
}
