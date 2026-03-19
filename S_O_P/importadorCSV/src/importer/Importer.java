package importer;


import model.Transaccion;
import model.Money;

import java.nio.file.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.*;

public class Importer {
    private final Path directorioBase;

    // Constructor que usa variable de entorno
    public Importer() {
        String baseDirEnv = System.getenv("IMPORT_BASE_DIR");
        String rutaBase = baseDirEnv != null ? baseDirEnv : "./data";

        // Capa 1: Variable de entorno define directorio permitido
        this.directorioBase = Paths.get(rutaBase).toAbsolutePath().normalize();

        // Crear el directorio si no existe
        try {
            Files.createDirectories(this.directorioBase);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio base: " + this.directorioBase, e);
        }

        System.out.println("📁 Directorio base permitido: " + this.directorioBase);
    }

    // Constructor para pruebas
    public Importer(Path directorioBase) {
        this.directorioBase = directorioBase.toAbsolutePath().normalize();
    }


    public ImportResult importar(String nombreArchivo) {
        Path archivoSeguro = validarRutaSegura(nombreArchivo);

        return procesarArchivo(archivoSeguro);
    }


    private Path validarRutaSegura(String nombreArchivo) {

        validarPatronesMaliciosos(nombreArchivo);


        Path rutaArchivo = Paths.get(nombreArchivo);
        String soloNombreArchivo = rutaArchivo.getFileName().toString();

        Path archivoSolicitado = directorioBase.resolve(soloNombreArchivo).normalize();

        try {
            Path rutaReal = archivoSolicitado.toRealPath(LinkOption.NOFOLLOW_LINKS);

            if (!rutaReal.startsWith(directorioBase)) {
                throw new SecurityException(
                        String.format("Acceso denegado: la ruta %s está fuera del directorio permitido %s",
                                rutaReal, directorioBase));
            }

            System.out.println("📄 Archivo a procesar: " + rutaReal);
            return rutaReal;

        } catch (NoSuchFileException e) {
            if (!archivoSolicitado.normalize().startsWith(directorioBase)) {
                throw new SecurityException("Acceso denegado: la ruta estaría fuera del directorio permitido");
            }

            System.out.println("📄 Archivo a procesar (no existe aún): " + archivoSolicitado);
            return archivoSolicitado;

        } catch (IOException e) {
            throw new RuntimeException("Error al validar ruta: " + archivoSolicitado, e);
        }
    }

    private void validarPatronesMaliciosos(String ruta) {
        String rutaNormalizada = ruta.replace('\\', '/');

        // Lista de patrones maliciosos
        String[] patronesMaliciosos = {
                "..",           // Directory traversal
                "./",           // Relativo al directorio actual
                "../",          // Subir directorio
                "~/",           // Home directory
                "~",            // Home directory (solo)
                "//",           // Doble slash (puede ser malicioso)
                "*",            // Wildcard
                "?",            // Wildcard
                ":",            // Windows drive letter
                "\\\\",         // Windows UNC paths
        };


        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            if (ruta.matches("^[a-zA-Z]:.*")) {
                throw new SecurityException("Acceso denegado: rutas absolutas de Windows no permitidas");
            }
        } else {
            if (ruta.startsWith("/")) {
                throw new SecurityException("Acceso denegado: rutas absolutas no permitidas: " + ruta);
            }
        }

        // Validar patrones maliciosos
        for (String patron : patronesMaliciosos) {
            if (rutaNormalizada.contains(patron)) {
                throw new SecurityException(
                        String.format("Acceso denegado: la ruta contiene patrón malicioso '%s': %s",
                                patron, ruta));
            }
        }

        // Validación adicional
        if (ruta.startsWith(".") && !ruta.startsWith("./")) {
            throw new SecurityException("Acceso denegado: la ruta no puede comenzar con punto: " + ruta);
        }
    }

    private ImportResult procesarArchivo(Path archivo) {
        System.out.println("🔄 Procesando archivo: " + archivo.getFileName());

        try {
            // Pipeline funcional: parse → validate → accumulate
            return Files.lines(archivo)
                    .skip(1)
                    .filter(linea -> !linea.trim().isEmpty())
                    .map(this::parsearLinea)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(
                            () -> ImportResult.builder(),
                            this::acumulador,
                            (b1, b2) -> { }
                    ).build();

        } catch (IOException e) {
            throw new RuntimeException("Error leyendo archivo: " + archivo, e);
        } catch (Exception e) {
            throw new RuntimeException("Error procesando archivo: " + e.getMessage(), e);
        }
    }


    /**
     * PASO 1: PARSE - Convierte una línea CSV en Optional<Transaccion>
     */
    private Optional<Transaccion> parsearLinea(String linea) {
        try {
            // Quitar comillas dobles y dividir por comas
            String[] campos = linea.split(",");

            // Validar número de campos
            if (campos.length < 4) {
                System.err.println("⚠️ Línea con campos insuficientes: " + linea);
                return Optional.empty();
            }

            // Limpiar comillas de cada campo
            String id = limpiarComillas(campos[0]);
            String tipo = limpiarComillas(campos[1]);
            String moneda = limpiarComillas(campos[2]);
            String valor = limpiarComillas(campos[3]);
            String descripcion = campos.length > 4 ? limpiarComillas(campos[4]) : "";

            // Validar campos obligatorios
            if (id.isEmpty() || tipo.isEmpty() || moneda.isEmpty() || valor.isEmpty()) {
                System.err.println("⚠️ Línea con campos vacíos: " + linea);
                return Optional.empty();
            }


            Transaccion transaccion = Transaccion.of(id, tipo, moneda, valor, descripcion);
            return Optional.of(transaccion);

        } catch (IllegalArgumentException e) {

            System.err.println("❌ Error de validación: " + e.getMessage() + " en línea: " + linea);
            return Optional.empty();
        } catch (Exception e) {

            System.err.println("❌ Error inesperado parseando línea: " + linea + " - " + e.getMessage());
            return Optional.empty();
        }
    }


    private boolean validarTransaccion(Transaccion t) {
        // Validaciones adicionales
        if (t.getMonto().getCantidad().scale() > 2) {
            System.err.println("⚠️ Monto con más de 2 decimales: " + t);
            return false;
        }

        // Validar que la moneda sea MXN o USD
        String moneda = t.getMonto().getMoneda();
        if (!moneda.equals("MXN") && !moneda.equals("USD")) {
            System.err.println("⚠️ Moneda no soportada: " + moneda + " - " + t);
            return false;
        }

        return true;
    }


    private String limpiarComillas(String campo) {
        if (campo == null) return "";
        return campo.replaceAll("^\"|\"$", "").trim();
    }


    private void acumulador(ImportResult.Builder builder, Transaccion transaccion) {
        // Validación adicional
        if (validarTransaccion(transaccion)) {
            builder.agregarTransaccionValida(transaccion);
        } else {
            builder.agregarError("Transacción no pasó validaciones extras: " + transaccion);
        }
    }


    // Getter para pruebas
    public Path getDirectorioBase() {
        return directorioBase;
    }
}
