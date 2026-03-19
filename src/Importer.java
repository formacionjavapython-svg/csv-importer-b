import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Importer {
    
    public List<Transaction> importCSV(String fileName) {
        List<Transaction> transactions = new ArrayList<>();
        
        try {
            List<String> lines = Files.readAllLines(
                Paths.get(fileName)
            );
            
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split(",");
                
                if (parts.length != 5) {
                    System.out.println("Error linea " + i);
                    continue;
                }
                
                String id = parts[0].trim();
                String type = parts[1].trim();
                double amount = Double.parseDouble(parts[2].trim());
                String currency = parts[3].trim();
                String description = parts[4].trim();
                
                Money money = new Money(amount, currency);
                Transaction tx = new Transaction(id, type, money, 
                                                 description);
                
                transactions.add(tx);
            }
            
            System.out.println("Se leyeron " + transactions.size() + 
                             " transacciones");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return transactions;
    }
    
    public double getTotalIN(List<Transaction> transactions) {
        double total = 0;
        for (Transaction tx : transactions) {
            if (tx.type.equals("IN")) {
                total = total + tx.money.amount;
            }
        }
        return total;
    }
    
    public double getTotalOUT(List<Transaction> transactions) {
        double total = 0;
        for (Transaction tx : transactions) {
            if (tx.type.equals("OUT")) {
                total = total + tx.money.amount;
            }
        }
        return total;
    }
}
