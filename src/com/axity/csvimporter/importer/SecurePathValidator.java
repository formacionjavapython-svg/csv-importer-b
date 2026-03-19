package com.axity.csvimporter.importer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SecurePathValidator {

    private final Path basePath;

    public SecurePathValidator() {
        // Leer variable de entorno IMPORT_BASE_DIR
        String baseDirEnv = System.getenv("IMPORT_BASE_DIR");

        if (baseDirEnv == null || baseDirEnv.isBlank()) {
            // Si no existe, usar el directorio actual como fallback (solo para desarrollo)
            baseDirEnv = System.getProperty("user.dir") + "/data";
            System.out.println("WARNING: IMPORT_BASE_DIR no definido, usando: " + baseDirEnv);
        }

        try {
            Path rawPath = Paths.get(baseDirEnv);

            // Crear el directorio si no existe
            if (!Files.exists(rawPath)) {
                System.out.println("Creando directorio base: " + rawPath);
                Files.createDirectories(rawPath);
            }

            // Ahora sí podemos obtener la ruta real (resolviendo symlinks)
            this.basePath = rawPath.toRealPath();
            System.out.println("✅ Directorio base seguro: " + this.basePath);

        } catch (IOException e) {
            throw new RuntimeException("No se puede acceder/crear el directorio base: " + baseDirEnv, e);
        }
    }

    /**
     * Valida que el nombre de archivo sea seguro y esté dentro del directorio permitido
     * @param fileName nombre del archivo (ej: "transacciones.csv")
     * @return Path absoluto y validado del archivo
     * @throws SecurityException si se detecta path traversal
     * @throws IOException si hay problemas de E/S
     */
    public Path validateAndResolve(String fileName) throws IOException, SecurityException {
        // 1. Validar nombre de archivo básico
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("El nombre del archivo no puede estar vacío");
        }

        // 2. Detectar patrones maliciosos básicos
        if (fileName.contains("..") ||
                fileName.startsWith("/") ||
                fileName.startsWith("\\") ||
                fileName.matches("^[A-Za-z]:.*") ||  // Detectar C:\ D:\ etc
                fileName.contains("../") ||
                fileName.contains("..\\")) {

            throw new SecurityException("⚠️ Posible path traversal detectado en: " + fileName);
        }

        // 3. Resolver la ruta completa de forma segura
        Path resolvedPath = basePath.resolve(fileName).normalize();

        // 4. Verificar que después de normalizar, sigue dentro del directorio base
        if (!resolvedPath.startsWith(basePath)) {
            throw new SecurityException("⚠️ Path traversal detectado: " + resolvedPath + " está fuera de " + basePath);
        }

        // 5. Verificar que el archivo existe
        if (!Files.exists(resolvedPath)) {
            throw new IOException("El archivo no existe: " + resolvedPath);
        }

        // 6. Verificar que es un archivo regular (no un directorio)
        if (!Files.isRegularFile(resolvedPath)) {
            throw new IOException("La ruta no es un archivo regular: " + resolvedPath);
        }

        // 7. Verificar permisos de lectura
        if (!Files.isReadable(resolvedPath)) {
            throw new IOException("No hay permisos de lectura para: " + resolvedPath);
        }

        return resolvedPath.toRealPath(); // Resolver symlinks por última vez
    }

    /**
     * Método de utilidad para obtener el directorio base
     */
    public Path getBasePath() {
        return basePath;
    }
}