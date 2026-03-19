import java.io.IOException;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("--- INICIANDO IMPORTACIÓN DE DATOS ---");

        try {
            Importer importer = new Importer();

            // Procesamos el archivo (Debe estar en la carpeta /data)
            ImportResult result = importer.processFile("transacciones.csv");

            System.out.println("--------------------------------------");
            System.out.println(" PROCESO EXITOSO");
            System.out.println(" Transacciones válidas: " + result.validCount);
            System.out.println(" Total Entradas (IN):  $" + result.totalIn);
            System.out.println(" Total Salidas (OUT):  $" + result.totalOut);
            System.out.println("--------------------------------------");

        } catch (ImportException e) {
            // Errores de lógica o configuración (Variable de entorno, Seguridad, Datos mal formados)
            System.err.println(" ERROR DE NEGOCIO: " + e.getMessage());
        } catch (IOException e) {
            // Errores de archivo (No existe, está bloqueado, etc.)
            System.err.println(" ERROR DE ARCHIVO: No se pudo leer el CSV. " + e.getMessage());
        } catch (Exception e) {
            // Cualquier otro error inesperado
            System.err.println(" ERROR INESPERADO: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("--- FIN DEL PROGRAMA ---");
    }
}