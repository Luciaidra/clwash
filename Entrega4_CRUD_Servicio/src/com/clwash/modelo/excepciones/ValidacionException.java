package com.clwash.modelo.excepciones;

/**
 * Se lanza cuando los datos ingresados por el usuario no cumplen las reglas
 * de negocio (campo vacío, patente con formato inválido, etc.).
 * <p>
 * Es una excepción de negocio, distinta de {@link DAOException}: el error
 * no es técnico, sino del dato, y el usuario lo puede corregir.
 * </p>
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class ValidacionException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepción con el mensaje que verá el usuario.
     *
     * @param mensaje explicación clara de qué dato está mal y por qué
     */
    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
