package com.clwash.modelo.casosdeuso.servicio;

import com.clwash.modelo.dao.ServicioDAO;
import com.clwash.modelo.excepciones.DAOException;
import com.clwash.modelo.excepciones.EntidadNoEncontradaException;
import com.clwash.modelo.excepciones.ValidacionException;

/**
 * Caso de uso: <b>Eliminar servicio</b> del catálogo.
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class EliminarServicio {

    /**
     * Ejecuta el caso de uso.
     *
     * @param id  id del servicio a eliminar
     * @param dao DAO con el que se persiste
     * @throws ValidacionException          si no se indicó un id válido
     * @throws EntidadNoEncontradaException si el servicio no existe
     * @throws DAOException                 si está usado en órdenes o hay un error de acceso a datos
     */
    public void ejecutar(int id, ServicioDAO dao) throws ValidacionException, DAOException {
        if (id <= 0) {
            throw new ValidacionException("Debe seleccionar un servicio para eliminar.");
        }
        dao.eliminar(id);
    }
}
