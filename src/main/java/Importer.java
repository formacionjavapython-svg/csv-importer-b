import java.io.BufferedReader;
import java.io.FileReader;

public class Importer {

    public void leerCSV(String ruta) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {

            String linea;

            while ((linea = br.readLine()) != null) {

                System.out.println("Leyendo registro: " + linea);

                Transaction tx = parsearLinea(linea);

                System.out.println("Registro procesado -> " + tx);
            }

        } catch (Exception e) {
            System.out.println("No se pudo leer el archivo: " + e.getMessage());
        }
    }

    private Transaction parsearLinea(String linea) {

        String[] partes = linea.split(",");

        String id = partes[0];
        TxType tipo = TxType.valueOf(partes[1]);
        String monto = partes[2];
        String moneda = partes[3];
        String descripcion = partes[4];

        Money dinero = new Money(monto, moneda);

        return new Transaction(id, tipo, dinero, descripcion);
    }
}