package com.escuela.csvimporter;

import com.escuela.csvimporter.importer.Importer;
import com.escuela.csvimporter.model.ImportResult;

public class Main {

    public static void main(String[] args) {
        Importer importer = new Importer();
        ImportResult result = importer.importFile("transactions.csv");

        System.out.println("=== RESUMEN DE IMPORTACIÓN ===");
        System.out.println("Válidos: " + result.getValidCount());
        System.out.println("Inválidos: " + result.getInvalidCount());
        System.out.println("Total IN: " + result.getTotalIn());
        System.out.println("Total OUT: " + result.getTotalOut());

        if (!result.getErrors().isEmpty()) {
            System.out.println("=== ERRORES ===");
            for (String error : result.getErrors()) {
                System.out.println(error);
            }
        }
    }
}