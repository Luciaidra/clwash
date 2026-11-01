package com.clwash.modelo.excepciones;

/**
 * Error técnico en el acceso a datos (falla de conexión, SQL inválido, etc.).
 * <p>
 * Envuelve a {@link java.sql.SQLException} para que las capas superiores
 * (casos de uso, controlador, vista) no dependan de JDBC.
 * </p>
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class DAOException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepción con el mensaje que verá el usuario.
     *
     * @param mensaje descripción del error para el usuario/desarrollador
     */
    public DAOException(String mensaje) {
        super(mensaje);
    }

    /**
     * Crea la excepción con el mensaje que verá el usuario.
     *
     * @param mensaje descripción del error
     * @param causa   excepción original (normalmente una {@code SQLException})
     */
    public DAOException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
