import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

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
}