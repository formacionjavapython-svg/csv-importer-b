package com.escuela.csvimporter.security;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathSecurity {

    public Path resolveSecurePath(String fileName) throws IOException {
        String baseDir = System.getenv("IMPORT_BASE_DIR");

        if (baseDir == null || baseDir.isBlank()) {
            throw new IllegalStateException("La variable IMPORT_BASE_DIR no está definida");
        }

        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("El nombre del archivo es obligatorio");
        }

        if (fileName.contains("..")
                || fileName.contains(":")
                || fileName.startsWith("/")
                || fileName.startsWith("\\")) {
            throw new SecurityException("Path traversal detectado");
        }

        Path basePath = Paths.get(baseDir).toRealPath();
        Path filePath = basePath.resolve(fileName).normalize().toRealPath();

        if (!filePath.startsWith(basePath)) {
            throw new SecurityException("El archivo está fuera del directorio permitido");
        }

        return filePath;
    }
}