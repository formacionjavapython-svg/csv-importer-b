package main.java;

import java.util.Scanner;

class Main {

    public static void main(String[] args) {

        System.out.println("Hello, World!");
        System.out.println("Test Arturo Perez Gomez");
        System.out.println("=================================");
        System.out.println("Importador CSV con seguridad");
        System.out.println("=================================");
        Importer importer = new Importer();
        Scanner sc = new Scanner(System.in);

        System.out.println("Ruta del CSV:");
        String filePath = sc.nextLine();
        System.out.println("....................");
        ImportResult resultado = importer.importCSV(filePath);

        resultado.resumen();
        System.out.println("Presiona Enter para salir...");
        sc.nextLine();
        sc.close();


    }
}