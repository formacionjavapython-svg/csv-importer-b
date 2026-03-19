import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;

public class Importer {

    public void leerCSV(String ruta) {

        int validos = 0;
        int invalidos = 0;
        BigDecimal ingresos = BigDecimal.ZERO;
        BigDecimal egresos = BigDecimal.ZERO;

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {

            String linea;

            while ((linea = br.readLine()) != null) {

                try {
                    Transaction tx = parsearLinea(linea);

                    validos++;

                    if (tx.type == TxType.IN) {
                        ingresos = ingresos.add(tx.amount.getAmount());
                    } else {
                        egresos = egresos.add(tx.amount.getAmount());
                    }

                } catch (Exception e) {
                    invalidos++;
                    System.out.println("Registro inválido: " + linea);
                }
            }

            System.out.println("------ Resumen de importación ------");
            System.out.println("Válidos: " + validos);
            System.out.println("Inválidos: " + invalidos);
            System.out.println("Total ingresos: " + ingresos);
            System.out.println("Total egresos: " + egresos);

        } catch (Exception e) {
            System.out.println("Error leer archivo: " + e.getMessage());
        }
    }

    private Transaction parsearLinea(String linea) {

        String[] partes = linea.split(",");

        if (partes.length < 5) {
            throw new RuntimeException("Formato incorrecto");
        }

        String id = partes[0];
        TxType tipo = TxType.valueOf(partes[1]);
        String monto = partes[2];
        String moneda = partes[3];
        String descripcion = partes[4];

        Money dinero = new Money(monto, moneda);

        return new Transaction(id, tipo, dinero, descripcion);
    }
}