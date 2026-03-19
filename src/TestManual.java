public class TestManual {
    public static void main(String[] args) {
        System.out.println("--- EJECUTANDO PRUEBAS DE VALIDACIÓN ---");
        Importer imp = new Importer();

        // Prueba 1: Archivo que NO existe
        try {
            imp.processFile("archivo_fantasma.csv");
            System.out.println(" Falló: Debería haber lanzado error de archivo no encontrado.");
        } catch (Exception e) {
            System.out.println("✅ Pasó: Detectó correctamente que el archivo no existe.");
        }

        // Prueba 2: Intento de salir de la carpeta (Seguridad)
        try {
            imp.processFile("../config.xml");
            System.out.println(" Falló: Debería haber bloqueado el acceso fuera de /data.");
        } catch (SecurityException e) {
            System.out.println(" Pasó: Bloqueó el intento de Path Traversal.");
        } catch (Exception e) {
            System.out.println("⚠ Nota: Error inesperado pero bloqueado: " + e.getMessage());
        }
    }
}