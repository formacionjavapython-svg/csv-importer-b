package com.seguro;

import com.seguro.model.ImportResult;
import com.seguro.service.Importer;

public class Main {

    public static void main(String[] args) {
        String fileName = "transacciones_prueba.csv";

        if (args.length > 0) {
            fileName = args[0];
        }

        Importer importer = new Importer();
        ImportResult result = importer.importFile(fileName);

        System.out.println("=== RESULTADO DE IMPORTACIÓN ===");
        System.out.println("Registros válidos: " + result.getValidCount());
        System.out.println("Registros inválidos: " + result.getInvalidCount());
        System.out.println("Total IN: " + result.getTotalIn());
        System.out.println("Total OUT: " + result.getTotalOut());

        if (!result.getErrors().isEmpty()) {
            System.out.println("\n--- ERRORES ---");
            for (String error : result.getErrors()) {
                System.out.println(error);
            }
        }
    }
}