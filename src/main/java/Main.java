package main.java;

import dto.ImportResult;
import service.Importer;

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
    }
}
