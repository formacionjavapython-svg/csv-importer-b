package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Importer {

    public ImportResult importarArchivo(String nombreArchivo) throws IOException {

        String directorioBase = System.getenv("IMPORT_BASE_DIR");


        if (directorioBase == null || directorioBase.isBlank()) {
            throw new IllegalStateException("IMPORT_BASE_DIR no está definida");
        }

        Path rutaBase = Paths.get(directorioBase).toRealPath();
        Path rutaArchivo = rutaBase.resolve(nombreArchivo).normalize();

        if (!rutaArchivo.startsWith(rutaBase)) {
            throw new SecurityException("Path traversal detectado");
        }

        ImportResult resultado = new ImportResult();


        try (BufferedReader lector = Files.newBufferedReader(rutaArchivo)) {

            String linea;

            while ((linea = lector.readLine()) != null) {

                try {

                    String[] partes = linea.split(",");

                    Transaction transaccion = new Transaction(
                            partes[0],
                            partes[1],
                            partes[2],
                            partes[3],
                            partes[4]
                    );

                    resultado.validos++;

                    if (transaccion.getTipo() == TxType.IN) {
                        resultado.totalIngresos += transaccion.getMoney().getValorMonetario();
                    } else {
                        resultado.totalEgresos += transaccion.getMoney().getValorMonetario();
                    }

                } catch (Exception e) {

                    resultado.invalidos++;
                    resultado.errores.add("Error en linea: " + linea);
                }
            }
        }

        return resultado;
    }
}