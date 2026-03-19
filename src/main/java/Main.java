package main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
     
        if (args.length < 1) {
            System.err.println("Uso: java Main <archivo.csv>");
            System.exit(1);
        }

     
        Path basePath = Paths.get("data").toAbsolutePath().normalize();

   
        try {
            Files.createDirectories(basePath);
        } catch (IOException e) {
            System.err.println("No se pudo crear el directorio base: " + e.getMessage());
            System.exit(1);
        }

  
        Importer importer = new Importer(basePath);
        String fileName = args[0];

        try {
      
            ImportResult result = importer.importFile(fileName);
       
            printResult(result);
        } catch (SecurityException e) {
            System.err.println("Error de seguridad: " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void printResult(ImportResult result) {
        System.out.println("=== Resumen de Importación ===");
        System.out.println("Registros válidos: " + result.getValidCount());
        System.out.println("Registros inválidos: " + result.getInvalidCount());
        System.out.println("Total IN: " + result.getTotalIn());
        System.out.println("Total OUT: " + result.getTotalOut());

        if (!result.getErrors().isEmpty()) {
            System.out.println("\nErrores:");
            for (String error : result.getErrors()) {
                System.out.println("  - " + error);
            }
        }
    }
}