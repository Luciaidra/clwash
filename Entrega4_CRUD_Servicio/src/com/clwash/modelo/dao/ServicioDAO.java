package com.clwash.modelo.dao;

import com.clwash.modelo.entidades.Servicio;
import com.clwash.modelo.excepciones.DAOException;

import java.util.List;

/**
 * DAO específico de {@link Servicio}.
 * <p>
 * Hereda el CRUD de {@link DAO} (con el id numérico como clave) y agrega
 * consultas propias del catálogo de servicios.
 * </p>
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public interface ServicioDAO extends DAO<Servicio, Integer> {

    /**
     * Busca servicios cuyo nombre o descripción contenga el texto indicado.
     *
     * @param texto texto a buscar (no distingue mayúsculas)
     * @return servicios que coinciden
     * @throws DAOException si hay un error de acceso a datos
     */
    List<Servicio> buscarPorTexto(String texto) throws DAOException;

    /**
     * Indica si ya existe otro servicio con el mismo nombre.
     * Se usa para no tener dos servicios llamados igual en el catálogo.
     *
     * @param nombre    nombre a verificar (no distingue mayúsculas)
     * @param idExcluir id del servicio que se está modificando, para no
     *                  compararlo consigo mismo; usar 0 en un alta
     * @return {@code true} si otro servicio ya usa ese nombre
     * @throws DAOException si hay un error de acceso a datos
     */
    boolean existeNombre(String nombre, int idExcluir) throws DAOException;
}
