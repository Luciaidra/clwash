package com.clwash.modelo.dao.impl;

import com.clwash.modelo.dao.ConexionBD;
import com.clwash.modelo.dao.VehiculoDAO;
import com.clwash.modelo.entidades.TipoVehiculo;
import com.clwash.modelo.entidades.Vehiculo;
import com.clwash.modelo.excepciones.DAOException;
import com.clwash.modelo.excepciones.EntidadDuplicadaException;
import com.clwash.modelo.excepciones.EntidadNoEncontradaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de {@link VehiculoDAO} con JDBC sobre MySQL.
 * <p>
 * Buenas prácticas aplicadas:
 * </p>
 * <ul>
 *   <li>{@link PreparedStatement} en todas las consultas (evita inyección SQL).</li>
 *   <li>try-with-resources para cerrar siempre conexión, sentencia y resultado.</li>
 *   <li>Las {@link SQLException} se traducen a excepciones propias del modelo.</li>
 *   <li>Las sentencias SQL están en constantes, separadas de la lógica.</li>
 * </ul>
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class VehiculoDAOJDBC implements VehiculoDAO {

    private static final String SQL_INSERT =
            "INSERT INTO vehiculo (patente, marca, modelo, tipo) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE =
            "UPDATE vehiculo SET marca = ?, modelo = ?, tipo = ? WHERE patente = ?";
    private static final String SQL_DELETE =
            "DELETE FROM vehiculo WHERE patente = ?";
    private static final String SQL_SELECT_BY_ID =
            "SELECT patente, marca, modelo, tipo FROM vehiculo WHERE patente = ?";
    private static final String SQL_SELECT_ALL =
            "SELECT patente, marca, modelo, tipo FROM vehiculo ORDER BY patente";
    private static final String SQL_SELECT_BY_TEXTO =
            "SELECT patente, marca, modelo, tipo FROM vehiculo "
            + "WHERE UPPER(patente) LIKE ? OR UPPER(marca) LIKE ? OR UPPER(modelo) LIKE ? "
            + "ORDER BY patente";
    private static final String SQL_SELECT_BY_TIPO =
            "SELECT patente, marca, modelo, tipo FROM vehiculo WHERE tipo = ? ORDER BY patente";
    private static final String SQL_EXISTE =
            "SELECT 1 FROM vehiculo WHERE patente = ?";

    /**
     * {@inheritDoc}
     *
     * @throws EntidadDuplicadaException si la patente ya está registrada
     */
    @Override
    public void insertar(Vehiculo v) throws DAOException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {
            ps.setString(1, v.getPatente());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setString(4, v.getTipo().name());
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new EntidadDuplicadaException(
                    "Ya existe un vehículo con la patente " + v.getPatente() + ".");
        } catch (SQLException e) {
            throw new DAOException("Error al guardar el vehículo " + v.getPatente() + ".", e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>La patente es la clave y no se modifica; se actualizan marca, modelo y tipo.</p>
     *
     * @throws EntidadNoEncontradaException si no existe un vehículo con esa patente
     */
    @Override
    public void modificar(Vehiculo v) throws DAOException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, v.getMarca());
            ps.setString(2, v.getModelo());
            ps.setString(3, v.getTipo().name());
            ps.setString(4, v.getPatente());
            if (ps.executeUpdate() == 0) {
                throw new EntidadNoEncontradaException(
                        "No existe un vehículo con la patente " + v.getPatente() + ".");
            }
        } catch (SQLException e) {
            throw new DAOException("Error al modificar el vehículo " + v.getPatente() + ".", e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws EntidadNoEncontradaException si no existe un vehículo con esa patente
     * @throws DAOException si el vehículo tiene órdenes de lavado asociadas
     */
    @Override
    public void eliminar(String patente) throws DAOException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setString(1, patente);
            if (ps.executeUpdate() == 0) {
                throw new EntidadNoEncontradaException(
                        "No existe un vehículo con la patente " + patente + ".");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            // Violación de la clave foránea en orden_lavado
            throw new DAOException("No se puede eliminar el vehículo " + patente
                    + " porque tiene órdenes de lavado registradas.", e);
        } catch (SQLException e) {
            throw new DAOException("Error al eliminar el vehículo " + patente + ".", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Vehiculo buscarPorId(String patente) throws DAOException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setString(1, patente);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar el vehículo " + patente + ".", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<Vehiculo> listarTodos() throws DAOException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            return mapearLista(rs);
        } catch (SQLException e) {
            throw new DAOException("Error al listar los vehículos.", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<Vehiculo> buscarPorTexto(String texto) throws DAOException {
        String patron = "%" + texto.toUpperCase() + "%";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_TEXTO)) {
            ps.setString(1, patron);
            ps.setString(2, patron);
            ps.setString(3, patron);
            try (ResultSet rs = ps.executeQuery()) {
                return mapearLista(rs);
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar vehículos.", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<Vehiculo> listarPorTipo(TipoVehiculo tipo) throws DAOException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_TIPO)) {
            ps.setString(1, tipo.name());
            try (ResultSet rs = ps.executeQuery()) {
                return mapearLista(rs);
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar vehículos por tipo.", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean existePatente(String patente) throws DAOException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_EXISTE)) {
            ps.setString(1, patente);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DAOException("Error al verificar la patente " + patente + ".", e);
        }
    }

    /**
     * Convierte la fila actual del {@link ResultSet} en un {@link Vehiculo}.
     */
    private Vehiculo mapear(ResultSet rs) throws SQLException {
        return new Vehiculo(
                rs.getString("patente"),
                rs.getString("marca"),
                rs.getString("modelo"),
                TipoVehiculo.valueOf(rs.getString("tipo")));
    }

    /**
     * Recorre todo el {@link ResultSet} y arma la lista de vehículos.
     */
    private List<Vehiculo> mapearLista(ResultSet rs) throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        while (rs.next()) {
            lista.add(mapear(rs));
        }
        return lista;
    }
}
