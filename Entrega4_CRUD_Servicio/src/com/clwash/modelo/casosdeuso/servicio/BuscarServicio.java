package com.clwash.modelo.casosdeuso.servicio;

import com.clwash.modelo.dao.ServicioDAO;
import com.clwash.modelo.entidades.Servicio;
import com.clwash.modelo.excepciones.DAOException;
import com.clwash.modelo.excepciones.EntidadNoEncontradaException;
import com.clwash.modelo.excepciones.ValidacionException;

/**
 * Caso de uso: <b>Consultar servicio por id</b>.
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class BuscarServicio {

    /**
     * Ejecuta el caso de uso.
     *
     * @param id  id del servicio
     * @param dao DAO de consulta
     * @return el servicio encontrado (nunca {@code null})
     * @throws ValidacionException          si el id no es válido
     * @throws EntidadNoEncontradaException si no existe
     * @throws DAOException                 si hay un error de acceso a datos
     */
    public Servicio ejecutar(int id, ServicioDAO dao) throws ValidacionException, DAOException {
        if (id <= 0) {
            throw new ValidacionException("El id del servicio no es válido.");
        }
        Servicio s = dao.buscarPorId(id);
        if (s == null) {
            throw new EntidadNoEncontradaException("No se encontró el servicio con id " + id + ".");
        }
        return s;
    }
}
