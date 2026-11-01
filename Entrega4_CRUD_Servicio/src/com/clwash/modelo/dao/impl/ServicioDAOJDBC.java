package com.clwash.modelo.dao.impl;

import com.clwash.modelo.dao.ConexionBD;
import com.clwash.modelo.dao.ServicioDAO;
import com.clwash.modelo.entidades.Servicio;
import com.clwash.modelo.excepciones.DAOException;
import com.clwash.modelo.excepciones.EntidadDuplicadaException;
import com.clwash.modelo.excepciones.EntidadNoEncontradaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de {@link ServicioDAO} con JDBC sobre MySQL.
 * <p>
 * Sigue las mismas prácticas que {@link VehiculoDAOJDBC}: {@link PreparedStatement},
 * try-with-resources y traducción de {@link SQLException} a excepciones del modelo.
 * Además, al insertar recupera el id autogenerado por la base.
 * </p>
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class ServicioDAOJDBC implements ServicioDAO {

    private static final String COLUMNAS = "id_servicio, nombre, descripcion, precio, duracion_min";

    private static final String SQL_INSERT =
            "INSERT INTO servicio (nombre, descripcion, precio, duracion_min) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE =
            "UPDATE servicio SET nombre = ?, descripcion = ?, precio = ?, duracion_min = ? "
            + "WHERE id_servicio = ?";
    private static final String SQL_DELETE =
            "DELETE FROM servicio WHERE id_servicio = ?";
    private static final String SQL_SELECT_BY_ID =
            "SELECT " + COLUMNAS + " FROM servicio WHERE id_servicio = ?";
    private static final String SQL_SELECT_ALL =
            "SELECT " + COLUMNAS + " FROM servicio ORDER BY nombre";
    private static final String SQL_SELECT_BY_TEXTO =
            "SELECT " + COLUMNAS + " FROM servicio "
            + "WHERE UPPER(nombre) LIKE ? OR UPPER(descripcion) LIKE ? ORDER BY nombre";
    private static final String SQL_EXISTE_NOMBRE =
            "SELECT 1 FROM servicio WHERE UPPER(nombre) = ? AND id_servicio <> ?";

    /**
     * {@inheritDoc}
     * <p>Después de insertar, le asigna al objeto el id generado por la base.</p>
     *
     * @throws EntidadDuplicadaException si ya existe un servicio con ese nombre
     */
    @Override
    public void insertar(Servicio s) throws DAOException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            cargarParametros(ps, s);
            ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    s.setId(claves.getInt(1));
                }
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new EntidadDuplicadaException("Ya existe un servicio llamado '" + s.getNombre() + "'.");
        } catch (SQLException e) {
            throw new DAOException("Error al guardar el servicio '" + s.getNombre() + "'.", e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws EntidadNoEncontradaException si no existe un servicio con ese id
     * @throws EntidadDuplicadaException    si el nuevo nombre ya lo usa otro servicio
     */
    @Override
    public void modificar(Servicio s) throws DAOException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
            cargarParametros(ps, s);
            ps.setInt(5, s.getId());
            if (ps.executeUpdate() == 0) {
                throw new EntidadNoEncontradaException("No existe el servicio con id " + s.getId() + ".");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new EntidadDuplicadaException("Ya existe un servicio llamado '" + s.getNombre() + "'.");
        } catch (SQLException e) {
            throw new DAOException("Error al modificar el servicio '" + s.getNombre() + "'.", e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws EntidadNoEncontradaException si no existe un servicio con ese id
     * @throws DAOException si el servicio está usado en órdenes de lavado
     */
    @Override
    public void eliminar(Integer id) throws DAOException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new EntidadNoEncontradaException("No existe el servicio con id " + id + ".");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DAOException("No se puede eliminar el servicio porque está usado "
                    + "en órdenes de lavado registradas.", e);
        } catch (SQLException e) {
            throw new DAOException("Error al eliminar el servicio con id " + id + ".", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Servicio buscarPorId(Integer id) throws DAOException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar el servicio con id " + id + ".", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<Servicio> listarTodos() throws DAOException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            return mapearLista(rs);
        } catch (SQLException e) {
            throw new DAOException("Error al listar los servicios.", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<Servicio> buscarPorTexto(String texto) throws DAOException {
        String patron = "%" + texto.toUpperCase() + "%";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_TEXTO)) {
            ps.setString(1, patron);
            ps.setString(2, patron);
            try (ResultSet rs = ps.executeQuery()) {
                return mapearLista(rs);
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar servicios.", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean existeNombre(String nombre, int idExcluir) throws DAOException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_EXISTE_NOMBRE)) {
            ps.setString(1, nombre.toUpperCase());
            ps.setInt(2, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DAOException("Error al verificar el nombre del servicio.", e);
        }
    }

    /** Carga nombre, descripción, precio y duración (parámetros 1 a 4). */
    private void cargarParametros(PreparedStatement ps, Servicio s) throws SQLException {
        ps.setString(1, s.getNombre());
        ps.setString(2, s.getDescripcion());
        ps.setBigDecimal(3, s.getPrecio());
        ps.setInt(4, s.getDuracionMinutos());
    }

    /** Convierte la fila actual del {@link ResultSet} en un {@link Servicio}. */
    private Servicio mapear(ResultSet rs) throws SQLException {
        return new Servicio(
                rs.getInt("id_servicio"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getBigDecimal("precio"),
                rs.getInt("duracion_min"));
    }

    /** Recorre todo el {@link ResultSet} y arma la lista de servicios. */
    private List<Servicio> mapearLista(ResultSet rs) throws SQLException {
        List<Servicio> lista = new ArrayList<>();
        while (rs.next()) {
            lista.add(mapear(rs));
        }
        return lista;
    }
}
