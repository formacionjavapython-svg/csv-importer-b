package src.main.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Importer {
    static void importar(){
        String filePath = "Pruebas/dinero.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;

        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
