package com.clwash.modelo.casosdeuso.servicio;

import com.clwash.modelo.dao.ServicioDAO;
import com.clwash.modelo.entidades.Servicio;
import com.clwash.modelo.excepciones.DAOException;
import com.clwash.modelo.excepciones.EntidadDuplicadaException;
import com.clwash.modelo.excepciones.ValidacionException;

/**
 * Caso de uso: <b>Registrar servicio</b> en el catálogo.
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class AltaServicio {

    /**
     * Ejecuta el caso de uso. Si termina bien, el servicio queda con el id asignado.
     *
     * @param servicio servicio a registrar
     * @param dao      DAO con el que se persiste (lo provee el controlador)
     * @throws ValidacionException       si algún dato es inválido
     * @throws EntidadDuplicadaException si ya hay un servicio con ese nombre
     * @throws DAOException              si hay un error de acceso a datos
     */
    public void ejecutar(Servicio servicio, ServicioDAO dao)
            throws ValidacionException, DAOException {
        ValidadorServicio.limpiar(servicio);
        ValidadorServicio.validar(servicio);
        if (dao.existeNombre(servicio.getNombre(), 0)) {
            throw new EntidadDuplicadaException(
                    "Ya existe un servicio llamado '" + servicio.getNombre() + "'.");
        }
        dao.insertar(servicio);
    }
}
