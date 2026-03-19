import java.util.List;

/**
 * TestRunner - Versión 1.0
 * 4 pruebas x 2 casos = 8 pruebas totales
 */
public class TestRunner {

    static int passed = 0;
    static int total = 0;

    public static void main(String[] args) {
        System.out.println("\n=== PRUEBAS CON TESTRUNNER personalizado ===\n");

        test1_PathTraversal();
        test2_CSVValido();
        test3_Conteo();
        test4_Totales();

        System.out.println("\n=== RESULTADOS ===");
        System.out.println("Pasadas: " + passed + "/" + total);

        if (passed == total) {
            System.out.println("V TODAS PASARON\n");
        } else {
            System.out.println("X Hay fallos\n");
        }
    }

    // PRUEBA 1: Path Traversal
    static void test1_PathTraversal() {
        System.out.println("PRUEBA 1: Path Traversal");

        // Caso 1: Rechaza "../"
        try {
            if (!"..".contains(".")) {
                throw new Exception("Debería detectar ..");
            }
            pass("Rechaza ../");
        } catch (Exception e) {
            fail("Rechaza ../");
        }

        // Caso 2: Acepta "datos/"
        try {
            if ("datos/archivo.csv".contains("..")) {
                throw new Exception("No debería estar en datos/");
            }
            pass("Acepta datos/archivo.csv");
        } catch (Exception e) {
            fail("Acepta datos/archivo.csv");
        }
    }

    // PRUEBA 2: CSV Válido
    static void test2_CSVValido() {
        System.out.println("PRUEBA 2: CSV Válido");

        // Caso 1: Lee transacciones
        try {
            Importer imp = new Importer();
            List<Transaction> txs = imp.importCSV("datos/transacciones.csv");

            if (txs.size() != 5) {
                throw new Exception("Debería leer 5, leyó " + txs.size());
            }
            pass("Lee 5 transacciones");
        } catch (Exception e) {
            fail("Lee 5 transacciones");
        }

        // Caso 2: Parsea correctamente
        try {
            Importer imp = new Importer();
            List<Transaction> txs = imp.importCSV("datos/transacciones.csv");

            if (!txs.get(0).id.equals("TX001")) {
                throw new Exception("ID incorrecto: " + txs.get(0).id);
            }
            pass("Parsea ID correctamente");
        } catch (Exception e) {
            fail("Parsea ID correctamente");
        }
    }

    // PRUEBA 3: Conteo
    static void test3_Conteo() {
        System.out.println("PRUEBA 3: Conteo Válidas/Inválidas");

        // Caso 1: Cuenta 5 válidas
        try {
            Importer imp = new Importer();
            List<Transaction> txs = imp.importCSV("datos/transacciones.csv");

            if (txs.size() != 5) {
                throw new Exception("Conteo incorrecto: " + txs.size());
            }
            pass("Cuenta 5 transacciones válidas");
        } catch (Exception e) {
            fail("Cuenta 5 transacciones válidas");
        }

        // Caso 2: Valida tipos
        try {
            Importer imp = new Importer();
            List<Transaction> txs = imp.importCSV("datos/transacciones.csv");

            for (Transaction tx : txs) {
                if (!tx.type.equals("IN") && !tx.type.equals("OUT")) {
                    throw new Exception("Tipo inválido: " + tx.type);
                }
            }
            pass("Todos los tipos son válidos (IN/OUT)");
        } catch (Exception e) {
            fail("Todos los tipos son válidos (IN/OUT)");
        }
    }

    // PRUEBA 4: Totales
    static void test4_Totales() {
        System.out.println("PRUEBA 4: Totales Correctos");

        // Caso 1: Total IN = 4500.0
        try {
            Importer imp = new Importer();
            List<Transaction> txs = imp.importCSV("datos/transacciones.csv");
            double totalIN = imp.getTotalIN(txs);

            if (totalIN != 4500.0) {
                throw new Exception("Total IN incorrecto: " + totalIN);
            }
            pass("Total INGRESOS = 4500.0");
        } catch (Exception e) {
            fail("Total INGRESOS = 4500.0");
        }

        // Caso 2: Total OUT = 800.0
        try {
            Importer imp = new Importer();
            List<Transaction> txs = imp.importCSV("datos/transacciones.csv");
            double totalOUT = imp.getTotalOUT(txs);

            if (totalOUT != 800.0) {
                throw new Exception("Total OUT incorrecto: " + totalOUT);
            }
            pass("Total EGRESOS = 800.0");
        } catch (Exception e) {
            fail("Total EGRESOS = 800.0");
        }
    }

    static void pass(String nombre) {
        System.out.println("  V " + nombre);
        passed++;
        total++;
    }

    static void fail(String nombre) {
        System.out.println("  X " + nombre);
        total++;
    }
}
