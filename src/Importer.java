<<<<<<< HEAD
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

/** Motor de importación con seguridad. */
public class Importer {
    public final ImportResult processFile(final String fileName) throws ImportException, IOException {
        final String baseDir = System.getenv("IMPORT_BASE_DIR");
        if (baseDir == null || baseDir.isEmpty()) {
            throw new ImportException("Variable IMPORT_BASE_DIR no definida.");
        }

        final Path basePath = Paths.get(baseDir).toRealPath();
        final Path filePath = basePath.resolve(fileName).normalize();

        if (!filePath.startsWith(basePath)) {
            throw new ImportException("SEGURIDAD: Acceso denegado.");
        }

        final ImportResult result = new ImportResult();
        try (Stream<String> lines = Files.lines(filePath)) {
            lines.skip(1).forEach(line -> {
                if (line == null || line.trim().isEmpty()) return;
                final String[] col = line.split(",");
                final TxType type = TxType.valueOf(col[1].toUpperCase());
                final Money money = new Money(col[2], col[3]);
                final Transaction tx = new Transaction(type, money);

                result.setValidCount(result.getValidCount() + 1);
                if (tx.getType() == TxType.IN) {
                    result.setTotalIn(result.getTotalIn().add(tx.getMoney().getAmount()));
                } else if (tx.getType() == TxType.OUT) {
                    result.setTotalOut(result.getTotalOut().add(tx.getMoney().getAmount()));
                }
            });
        } catch (Exception e) {
            throw new ImportException("ERROR EN EL ARCHIVO: " + filePath.getFileName());
        }
        return result;
    }
=======
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class Importer {
    /**
     * Procesa el archivo CSV buscando en la ruta configurada en variables de entorno.
     * @param fileName Nombre del archivo (ej. transacciones.csv)
     * @return ImportResult con los totales calculados
     * @throws ImportException Si hay errores de configuración o seguridad
     * @throws IOException Si el archivo no existe o no se puede leer
     */
    public ImportResult processFile(String fileName) throws ImportException, IOException {
        // 1. Validar variable de entorno
        String baseDir = System.getenv("IMPORT_BASE_DIR");
        if (baseDir == null || baseDir.isEmpty()) {
            throw new ImportException("ERROR: La variable de entorno IMPORT_BASE_DIR no está definida.");
        }

        // 2. Validar rutas y seguridad (Anti-Path Traversal)
        Path basePath = Paths.get(baseDir).toRealPath();
        Path filePath = basePath.resolve(fileName).normalize();

        if (!filePath.startsWith(basePath)) {
            throw new ImportException("SEGURIDAD: Intento de acceso fuera del directorio permitido.");
        }

        // 3. Procesar el archivo
        ImportResult result = new ImportResult();
        try (Stream<String> lines = Files.lines(filePath)) {
            lines.skip(1).forEach(line -> {
                if (line.trim().isEmpty()) return; // Ignorar líneas vacías

                String[] columns = line.split(",");
                // Se asume el formato: id, type, amount, currency
                TxType type = TxType.valueOf(columns[1].toUpperCase());
                Money money = new Money(columns[2], columns[3]);

                Transaction tx = new Transaction(type, money);
                result.validCount++;

                if (tx.getType() == TxType.IN) {
                    result.totalIn = result.totalIn.add(tx.getMoney().getAmount());
                } else if (tx.getType() == TxType.OUT) {
                    result.totalOut = result.totalOut.add(tx.getMoney().getAmount());
                }
            });
        } catch (IllegalArgumentException e) {
            throw new ImportException("ERROR DE DATOS: Tipo de transacción no válido en el archivo.");
        }

        return result;
    }
>>>>>>> 9b6df89a2299982d679daada700f55ff0526305a
}