package com.clwash.modelo.casosdeuso.vehiculo;

import com.clwash.modelo.dao.VehiculoDAO;
import com.clwash.modelo.entidades.Vehiculo;
import com.clwash.modelo.excepciones.DAOException;
import com.clwash.modelo.excepciones.EntidadNoEncontradaException;
import com.clwash.modelo.excepciones.ValidacionException;

/**
 * Caso de uso: <b>Eliminar vehículo</b>.
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class EliminarVehiculo {

    /**
     * Ejecuta el caso de uso.
     *
     * @param patente patente del vehículo a eliminar
     * @param dao     DAO con el que se persiste
     * @throws ValidacionException          si no se indicó la patente
     * @throws EntidadNoEncontradaException si el vehículo no existe
     * @throws DAOException                 si tiene órdenes asociadas o hay un error de acceso a datos
     */
    public void ejecutar(String patente, VehiculoDAO dao)
            throws ValidacionException, DAOException {
        String normalizada = Vehiculo.normalizarPatente(patente);
        if (normalizada == null || normalizada.isEmpty()) {
            throw new ValidacionException("Debe seleccionar un vehículo para eliminar.");
        }
        dao.eliminar(normalizada);
    }
}
