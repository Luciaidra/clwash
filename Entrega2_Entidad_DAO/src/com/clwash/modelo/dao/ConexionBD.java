package com.clwash.modelo.dao;

import com.clwash.modelo.excepciones.DAOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Centraliza los datos de conexión a la base de datos MySQL.
 * <p>
 * Cada operación del DAO pide una conexión nueva con {@link #getConexion()}
 * y la cierra con try-with-resources. Así nunca quedan conexiones abiertas
 * aunque ocurra una excepción.
 * </p>
 * <p>
 * <b>Configurar</b> {@code USUARIO} y {@code CLAVE} según la instalación local.
 * </p>
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public final class ConexionBD {

    private static final String URL =
            "jdbc:mysql://localhost:3306/clwash?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires";
    private static final String USUARIO = "root";
    private static final String CLAVE = "root";

    /** Clase utilitaria: no se instancia. */
    private ConexionBD() {
    }

    /**
     * Abre una conexión nueva a la base de datos.
     *
     * @return conexión abierta (quien la pide debe cerrarla)
     * @throws DAOException si no se puede conectar (servidor apagado,
     *                      credenciales incorrectas, base inexistente, etc.)
     */
    public static Connection getConexion() throws DAOException {
        try {
            return DriverManager.getConnection(URL, USUARIO, CLAVE);
        } catch (SQLException e) {
            throw new DAOException(
                    "No se pudo conectar con la base de datos. "
                    + "Verifique que MySQL esté iniciado y que exista la base 'clwash'.", e);
        }
    }
}
