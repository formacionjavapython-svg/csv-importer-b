import java.util.List;

public class Main {
    
    public static void main(String[] args) {
        System.out.println("\n=== CSV IMPORTER ===\n");
        
        Importer importer = new Importer();
        
        System.out.println("Leyendo: datos/transacciones.csv");
        List<Transaction> transactions = 
            importer.importCSV("datos/transacciones.csv");
        
        System.out.println("\nTransacciones:\n");
        for (Transaction tx : transactions) {
            System.out.println(tx);
        }
        
        double totalIN = importer.getTotalIN(transactions);
        double totalOUT = importer.getTotalOUT(transactions);
        
        System.out.println("\n=== RESUMEN ===");
        System.out.println("Total procesadas: " + transactions.size());
        System.out.println("Total INGRESOS: " + totalIN);
        System.out.println("Total EGRESOS: " + totalOUT);
        System.out.println("BALANCE: " + (totalIN - totalOUT));
        System.out.println("\n");
    }
}
