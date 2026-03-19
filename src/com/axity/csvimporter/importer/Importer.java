package com.axity.csvimporter.importer;

import com.axity.csvimporter.model.Transaction;
import com.axity.csvimporter.result.ImportResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Importer {

    private final SecurePathValidator pathValidator;

    public Importer() {
        this.pathValidator = new SecurePathValidator();
    }

    /**
     * Procesa un archivo CSV de forma segura
     * @param fileName nombre del archivo (ej: "datos.csv")
     * @return ImportResult con el resumen del procesamiento
     */
    public ImportResult processFile(String fileName) {
        ImportResult result = ImportResult.empty();

        try {
            // 1. Validar ruta de forma segura
            Path safePath = pathValidator.validateAndResolve(fileName);
            System.out.println("📂 Procesando archivo seguro: " + safePath);

            // 2. Leer todas las líneas del archivo
            List<String> lines = Files.readAllLines(safePath);
            System.out.println("✅ Archivo leído correctamente. Líneas totales: " + lines.size());

            // 3. PIPELINE DE PROCESAMIENTO
            processLines(lines, result);

        } catch (SecurityException e) {
            System.err.println("🔒 ERROR DE SEGURIDAD: " + e.getMessage());
            result.addError("Bloqueado por seguridad: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("📁 ERROR DE E/S: " + e.getMessage());
            result.addError("Error de archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ ERROR INESPERADO: " + e.getMessage());
            result.addError("Error inesperado: " + e.getMessage());
        }

        return result;
    }

    /**
     * Pipeline funcional: Parse → Validate → Accumulate
     */
    private void processLines(List<String> lines, ImportResult result) {
        if (lines.isEmpty()) {
            result.addError("El archivo está vacío");
            return;
        }

        // Asumimos que la primera línea es el header
        String header = lines.get(0);
        System.out.println("📄 Header detectado: " + header);

        // Validar header (opcional pero recomendado)
        if (!isValidHeader(header)) {
            result.addError("Header inválido. Se esperaba: Id,Tipo_Eptrada,Moneda,Valor");
            // Continuamos igual, pero registramos el warning
        }

        // Procesar cada línea de datos (desde línea 1 en adelante, saltando header)
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            int lineNumber = i + 1; // Para mensajes de error más claros

            // ETAPA 1: PARSE
            String[] fields = parseLine(line, lineNumber, result);
            if (fields == null) {
                continue; // Error ya registrado en parseLine
            }

            // ETAPA 2: VALIDATE Y CREATE (Transaction.of ya valida)
            try {
                // Extraer campos según el formato: Id,Tipo_Eptrada,Moneda,Valor
                // Nota: Ignoramos espacios en blanco alrededor de cada campo
                String id = fields[0].trim();
                String tipo = fields[1].trim();
                String moneda = fields[2].trim();
                String valor = fields[3].trim();

                // La descripción no está en el CSV, usamos cadena vacía
                Transaction transaction = Transaction.of(id, tipo, valor, moneda, "");

                // ETAPA 3: ACCUMULATE
                result.addValidTransaction(transaction);
                System.out.println("  ✅ Línea " + lineNumber + ": " + transaction);

            } catch (IllegalArgumentException e) {
                // Error de validación
                result.addError(lineNumber, "Datos inválidos: " + e.getMessage() + " | Línea: " + line);
                System.out.println("  ❌ Línea " + lineNumber + ": " + e.getMessage());
            }
        }
    }

    /**
     * Parsea una línea CSV respetando comas y posibles espacios
     * @return String[] con los campos o null si hay error
     */
    private String[] parseLine(String line, int lineNumber, ImportResult result) {
        if (line == null || line.isBlank()) {
            result.addError(lineNumber, "Línea vacía");
            return null;
        }

        // Dividir por coma (CSV simple, sin comillas escapadas por ahora)
        String[] fields = line.split(",", -1); // -1 para mantener campos vacíos

        // Verificar que tenemos exactamente 4 campos (Id, Tipo, Moneda, Valor)
        if (fields.length != 4) {
            result.addError(lineNumber, "Número incorrecto de campos. Se esperaban 4, se encontraron " + fields.length);
            return null;
        }

        return fields;
    }

    /**
     * Valida que el header tenga el formato esperado
     */
    private boolean isValidHeader(String header) {
        if (header == null || header.isBlank()) {
            return false;
        }

        // Más flexible: solo verificar que contenga las palabras clave
        String headerLower = header.toLowerCase();
        return headerLower.contains("id") &&
                (headerLower.contains("tipo") || headerLower.contains("tipo_entrada")) &&
                headerLower.contains("moneda") &&
                (headerLower.contains("valor") || headerLower.contains("monto"));
    }

    /**
     * Punto de entrada principal
     */
    public static void main(String[] args) {
        System.out.println("🔐 === Importador CSV Seguro - Pipeline Completo === 🔐\n");

        Importer importer = new Importer();

        System.out.println("🔷 --- PROCESANDO ARCHIVO VÁLIDO: transacciones.csv ---");
        ImportResult result1 = importer.processFile("transacciones.csv");
        result1.printSummary();

        System.out.println("\n🔷 --- PROCESANDO ARCHIVO CON ERRORES: errores.csv ---");
        ImportResult result2 = importer.processFile("errores.csv");
        result2.printSummary();

        System.out.println("\n🔷 --- PROCESANDO ARCHIVO CON PATH TRAVERSAL (bloqueado) ---");
        ImportResult result3 = importer.processFile("../etc/passwd");
        result3.printSummary();
    }
}