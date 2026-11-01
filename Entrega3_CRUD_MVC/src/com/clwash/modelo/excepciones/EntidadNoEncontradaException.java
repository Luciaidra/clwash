package com.clwash.modelo.excepciones;

/**
 * Se lanza cuando se busca, modifica o elimina una entidad que no existe.
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class EntidadNoEncontradaException extends DAOException {

    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepción con el mensaje que verá el usuario.
     *
     * @param mensaje descripción del problema
     */
    public EntidadNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
