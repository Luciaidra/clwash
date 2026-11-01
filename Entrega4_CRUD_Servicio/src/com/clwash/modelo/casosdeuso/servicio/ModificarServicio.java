package com.clwash.modelo.casosdeuso.servicio;

import com.clwash.modelo.dao.ServicioDAO;
import com.clwash.modelo.entidades.Servicio;
import com.clwash.modelo.excepciones.DAOException;
import com.clwash.modelo.excepciones.EntidadDuplicadaException;
import com.clwash.modelo.excepciones.EntidadNoEncontradaException;
import com.clwash.modelo.excepciones.ValidacionException;

/**
 * Caso de uso: <b>Modificar servicio</b> (nombre, descripción, precio o duración).
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class ModificarServicio {

    /**
     * Ejecuta el caso de uso.
     *
     * @param servicio servicio con los datos nuevos (debe tener id)
     * @param dao      DAO con el que se persiste
     * @throws ValidacionException          si algún dato es inválido o falta el id
     * @throws EntidadDuplicadaException    si el nuevo nombre ya lo usa otro servicio
     * @throws EntidadNoEncontradaException si el servicio no existe
     * @throws DAOException                 si hay un error de acceso a datos
     */
    public void ejecutar(Servicio servicio, ServicioDAO dao)
            throws ValidacionException, DAOException {
        ValidadorServicio.limpiar(servicio);
        ValidadorServicio.validar(servicio);
        if (servicio.getId() <= 0) {
            throw new ValidacionException("Debe seleccionar un servicio para modificar.");
        }
        if (dao.existeNombre(servicio.getNombre(), servicio.getId())) {
            throw new EntidadDuplicadaException(
                    "Ya existe otro servicio llamado '" + servicio.getNombre() + "'.");
        }
        dao.modificar(servicio);
    }
}
