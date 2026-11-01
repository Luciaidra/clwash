package com.clwash.modelo.dao;

import com.clwash.modelo.excepciones.DAOException;

import java.util.List;

/**
 * Interfaz DAO genérica con las operaciones CRUD comunes a todas las entidades.
 *
 * @param <T> tipo de la entidad (por ejemplo {@code Vehiculo})
 * @param <K> tipo de la clave primaria (por ejemplo {@code String} para la patente)
 * @author Lucía Mangioni, Catalina Baviello
 */
public interface DAO<T, K> {

    /**
     * Guarda una entidad nueva.
     *
     * @param entidad entidad a persistir
     * @throws DAOException si la entidad ya existe o hay un error de acceso a datos
     */
    void insertar(T entidad) throws DAOException;

    /**
     * Actualiza una entidad existente.
     *
     * @param entidad entidad con los datos modificados
     * @throws DAOException si la entidad no existe o hay un error de acceso a datos
     */
    void modificar(T entidad) throws DAOException;

    /**
     * Elimina la entidad con la clave indicada.
     *
     * @param id clave de la entidad a eliminar
     * @throws DAOException si la entidad no existe o hay un error de acceso a datos
     */
    void eliminar(K id) throws DAOException;

    /**
     * Busca una entidad por su clave.
     *
     * @param id clave a buscar
     * @return la entidad encontrada, o {@code null} si no existe
     * @throws DAOException si hay un error de acceso a datos
     */
    T buscarPorId(K id) throws DAOException;

    /**
     * Devuelve todas las entidades guardadas.
     *
     * @return lista (posiblemente vacía, nunca {@code null})
     * @throws DAOException si hay un error de acceso a datos
     */
    List<T> listarTodos() throws DAOException;
}
