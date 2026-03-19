package com.csvimporter;

import java.io.IOException;

/**
 * Clase de entrada para ejecutar el importador desde línea de comandos.
 *
 * <p>Requiere que se defina la variable de entorno IMPORT_BASE_DIR. El primer argumento debe
 * ser el nombre del archivo CSV a procesar (relativo al directorio base). Imprime por
 * consola el resumen con el número de registros válidos e inválidos y los totales.
 */
public final class Main {
    private Main() {
        // Evitar instanciar esta clase
    }

    /**
     * Punto de entrada de la aplicación.
     *
     * <p>Lee el nombre del archivo CSV del primer argumento de la línea de comandos,
     * crea una instancia de {@link Importer} utilizando la variable de entorno
     * {@code IMPORT_BASE_DIR} y procesa el archivo. Imprime un resumen del
     * resultado en la salida estándar. Si ocurre un error de seguridad o E/S,
     * el mensaje se muestra por la salida de error y el programa termina con
     * un código no cero.</p>
     *
     * @param args arreglo de argumentos de la línea de comandos; se espera un único elemento con el nombre del archivo CSV
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Uso: java Main <nombre_archivo_csv>");
            System.exit(1);
        }
        String csvFileName = args[0];
        try {
            Importer importer = new Importer();
            ImportResult importResult = importer.importCsvFile(csvFileName);
            System.out.println("Registros válidos: " + importResult.getValidTransactionCount());
            System.out.println("Registros inválidos: " + importResult.getInvalidTransactionCount());
            System.out.println("Total ingresos (IN): " + importResult.getTotalIncome());
            System.out.println("Total egresos (OUT): " + importResult.getTotalExpense());
            if (!importResult.getErrorMessages().isEmpty()) {
                System.out.println("Errores:");
                importResult.getErrorMessages().forEach(System.out::println);
            }
        } catch (SecurityException | IOException ex) {
            System.err.println("Error al importar: " + ex.getMessage());
            System.exit(2);
        }
    }
}