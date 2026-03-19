package com.csvimporter;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Clase principal que orquesta la importación de archivos CSV.
 *
 * <p>Realiza validación segura de rutas para evitar path traversal, lee el archivo línea por línea,
 * parsea y valida cada transacción, y acumula los resultados en un {@link ImportResult}.</p>
 */
public final class Importer {
    /**
     * Ruta del directorio base permitido para la importación.
     */
    private final Path baseDirectoryPath;

    /**
     * Crea un importador usando la variable de entorno IMPORT_BASE_DIR como directorio base.
     *
     * @throws IllegalStateException si la variable de entorno no está definida o es inválida
     */
    public Importer() {
        this(System.getenv("IMPORT_BASE_DIR"));
    }

    /**
     * Crea un importador con un directorio base explícito.
     *
     * @param baseDirectory ruta absoluta o relativa del directorio permitido; no puede ser nulo ni vacío.
     *                      Si la ruta es relativa, se resolverá a su representación real mediante {@link java.nio.file.Path#toRealPath()}.
     * @throws IllegalStateException si el directorio base no existe, no es un directorio o no se puede resolver
     */
    public Importer(String baseDirectory) {
        Objects.requireNonNull(baseDirectory, "IMPORT_BASE_DIR no definido");
        if (baseDirectory.isBlank()) {
            throw new IllegalStateException("IMPORT_BASE_DIR está vacío");
        }
        try {
            Path resolvedBaseDirectory = Paths.get(baseDirectory).toRealPath();
            if (!Files.isDirectory(resolvedBaseDirectory)) {
                throw new IllegalStateException("El directorio base no es un directorio: " + baseDirectory);
            }
            this.baseDirectoryPath = resolvedBaseDirectory;
        } catch (IOException exception) {
            throw new IllegalStateException("Error al resolver el directorio base: " + baseDirectory, exception);
        }
    }

    /**
     * Importa un archivo CSV desde el directorio permitido.
     *
     * @param fileName nombre del archivo dentro del directorio base
     * @return resultado de la importación con conteos y totales
     * @throws IOException        si ocurre un error de E/S al leer el archivo
     * @throws SecurityException  si se detecta path traversal u otra ruta no permitida
     */
    public ImportResult importCsvFile(String csvFileName) throws IOException {
        validateCsvFileName(csvFileName);
        // Construir la ruta segura
        Path resolvedCsvPath = baseDirectoryPath.resolve(csvFileName).normalize();
        if (!resolvedCsvPath.startsWith(baseDirectoryPath)) {
            throw new SecurityException("Se detectó un intento de path traversal: " + csvFileName);
        }
        if (!Files.exists(resolvedCsvPath) || !Files.isRegularFile(resolvedCsvPath)) {
            throw new IOException("El archivo no existe: " + resolvedCsvPath);
        }
        ImportResult importResult = new ImportResult();
        // Leer el archivo con un BufferedReader para eficiencia
        try (BufferedReader bufferedReader = Files.newBufferedReader(resolvedCsvPath, StandardCharsets.UTF_8)) {
            String currentLine;
            int lineNumber = 0;
            while ((currentLine = bufferedReader.readLine()) != null) {
                lineNumber++;
                // Omitir líneas en blanco
                if (currentLine.isBlank()) {
                    continue;
                }
                try {
                    Transaction transaction = parseLine(currentLine);
                    importResult.registerValidTransaction(transaction);
                } catch (IllegalArgumentException ex) {
                    // Registrar la línea como inválida
                    importResult.registerError("Línea " + lineNumber + ": " + ex.getMessage());
                }
            }
        }
        return importResult;
    }

    /**
     * Valida el nombre del archivo CSV para asegurar que no contenga rutas absolutas ni patrones
     * maliciosos como path traversal. Esta verificación previene intentos de acceder a archivos
     * fuera del directorio base permitido.
     *
     * @param csvFileName nombre del archivo proporcionado por el usuario
     * @throws SecurityException si el nombre del archivo es nulo, vacío, una ruta absoluta o contiene secuencias no permitidas
     */
    private void validateCsvFileName(String csvFileName) {
        Objects.requireNonNull(csvFileName, "El nombre del archivo no puede ser nulo");
        if (csvFileName.isBlank()) {
            throw new SecurityException("El nombre del archivo está vacío");
        }
        // No se permiten rutas absolutas ni patrones maliciosos básicos
        if (Paths.get(csvFileName).isAbsolute()) {
            throw new SecurityException("No se permiten rutas absolutas: " + csvFileName);
        }
        if (csvFileName.contains("..")) {
            throw new SecurityException("Se detectó un patrón de path traversal en: " + csvFileName);
        }
        // Rechazar rutas con caracteres sospechosos (ej. : en Windows)
        if (csvFileName.contains(":")) {
            throw new SecurityException("Carácter no permitido en la ruta: " + csvFileName);
        }
    }

    /**
     * Parsea una línea del CSV y construye una transacción.
     *
     * @param line línea de texto del archivo
     * @return transacción validada
     * @throws IllegalArgumentException si los datos son inválidos
     */
    /**
     * Analiza una línea del CSV y construye una instancia de {@link Transaction} a partir de sus campos.
     *
     * @param csvLine línea del archivo CSV, no debe ser nula
     * @return instancia de {@link Transaction} con los datos validados
     * @throws IllegalArgumentException si el número de columnas es incorrecto o algún campo es inválido
     */
    private Transaction parseLine(String csvLine) {
        String[] fields = csvLine.split(",", -1); // incluir campos vacíos
        if (fields.length < 5) {
            throw new IllegalArgumentException("Número de columnas incorrecto: " + csvLine);
        }
        String transactionId = fields[0].trim();
        String transactionTypeString = fields[1].trim();
        String amountString = fields[2].trim();
        String currencyCode = fields[3].trim();
        // La descripción puede contener comas, las reunimos a partir de la quinta columna
        StringBuilder descriptionBuilder = new StringBuilder();
        for (int index = 4; index < fields.length; index++) {
            if (index > 4) {
                descriptionBuilder.append(',');
            }
            descriptionBuilder.append(fields[index]);
        }
        String transactionDescription = descriptionBuilder.toString().trim();
        // Validaciones de campos
        if (transactionId.isEmpty()) {
            throw new IllegalArgumentException("El identificador está vacío");
        }
        TxType transactionType;
        try {
            transactionType = TxType.valueOf(transactionTypeString);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Tipo de transacción inválido: " + transactionTypeString);
        }
        Money monetaryAmount;
        try {
            monetaryAmount = new Money(new BigDecimal(amountString), currencyCode);
        } catch (Exception exception) {
            // Reutilizar mensaje de la excepción subyacente
            throw new IllegalArgumentException("Monto o moneda inválidos: " + exception.getMessage());
        }
        return new Transaction(transactionId, transactionType, monetaryAmount, transactionDescription);
    }
}