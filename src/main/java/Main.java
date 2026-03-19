package main.java;

import dto.ImportResult;
import service.Importer;

import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Money money = new Money("100.50", "MXN");
        System.out.println(money);

        Transaction tx = new Transaction(
                "1",
                TxType.IN,
                money,
                "Salario"
        );

        System.out.println(tx);


        Importer importer = new Importer();

        ImportResult result = importer.importFile("data.csv");

        System.out.println(result);

        try (FileWriter writer = new FileWriter("test-report.txt")) {
            writer.write(String.valueOf(result));
        } catch (IOException e) {
            System.out.println("Error writing report: " + e.getMessage());
        }
    }
}
