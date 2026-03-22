/** Excepción para el control de errores. */
public class ImportException extends Exception { // <--- Inicia la clase
    public ImportException(final String message) { // <--- Inicia el constructor
        super(message);
    } // <--- Cierra el constructor
} // <--- Cierra la clase (¡Esta llave es vital!)