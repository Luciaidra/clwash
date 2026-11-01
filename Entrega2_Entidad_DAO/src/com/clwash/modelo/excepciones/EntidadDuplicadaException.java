package com.clwash.modelo.excepciones;

/**
 * Se lanza al intentar dar de alta una entidad cuya clave ya existe
 * (por ejemplo, un vehículo con una patente ya registrada).
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class EntidadDuplicadaException extends DAOException {

    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepción con el mensaje que verá el usuario.
     *
     * @param mensaje descripción del problema
     */
    public EntidadDuplicadaException(String mensaje) {
        super(mensaje);
    }
}
