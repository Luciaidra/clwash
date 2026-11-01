package com.clwash.modelo.dao;

import com.clwash.modelo.entidades.TipoVehiculo;
import com.clwash.modelo.entidades.Vehiculo;
import com.clwash.modelo.excepciones.DAOException;

import java.util.List;

/**
 * DAO específico de {@link Vehiculo}.
 * <p>
 * Hereda el CRUD de {@link DAO} (con la patente como clave) y agrega
 * consultas propias de esta entidad.
 * </p>
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public interface VehiculoDAO extends DAO<Vehiculo, String> {

    /**
     * Busca vehículos cuya patente, marca o modelo contenga el texto indicado.
     *
     * @param texto texto a buscar (no distingue mayúsculas)
     * @return vehículos que coinciden
     * @throws DAOException si hay un error de acceso a datos
     */
    List<Vehiculo> buscarPorTexto(String texto) throws DAOException;

    /**
     * Devuelve los vehículos de un tipo determinado.
     *
     * @param tipo tipo de vehículo
     * @return vehículos de ese tipo
     * @throws DAOException si hay un error de acceso a datos
     */
    List<Vehiculo> listarPorTipo(TipoVehiculo tipo) throws DAOException;

    /**
     * Indica si ya hay un vehículo con esa patente.
     *
     * @param patente patente a verificar
     * @return {@code true} si existe
     * @throws DAOException si hay un error de acceso a datos
     */
    boolean existePatente(String patente) throws DAOException;
}
