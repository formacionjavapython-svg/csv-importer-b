package src.main.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Importer {
    private String id;
    private int monto;
    private boolean operacion;
    private String descString;
    private Money cuenta;

    public void importar(String file , Money cuenta){
        if (file.contains("..")){
            ImportResult revisor = new ImportResult();
            revisor.crearReporte("Directorio inválido");
            return;
        }
        String filePath = file;
        int cantidad = 0;
        boolean sumar = false;
        String a;
        List<String> parser = new ArrayList<>();;


        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;

        while ((line = br.readLine()) != null) {
            System.out.println(line);
            parser.add(line); 
        }
        }catch (IOException e) {
            String error = e.getMessage();
            ImportResult revisor = new ImportResult();
            revisor.crearReporte(error);
        }

        parser.remove(0);

        for(String element : parser){
            List<String> object1 = List.of(element.split(","));
            if (object1.contains("+")){
                    sumar = true;
                }
                else{
                    sumar = false;
                }
            operacion = sumar;
            id = object1.get(0);
            monto = Integer.parseInt(object1.get(2));
            descString = object1.get(3);
            this.cuenta = cuenta;
            hacerOperaciones();
        }        
    }

    private void hacerOperaciones(){
        Transaction transaction = new Transaction(
            id,
            operacion,
            monto,
            descString,
            cuenta);     
    }

    public int mostrarSaldo(){
        return cuenta.getMoneda();
    }
    
}
