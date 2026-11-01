package com.clwash.modelo.casosdeuso.vehiculo;

import com.clwash.modelo.dao.VehiculoDAO;
import com.clwash.modelo.entidades.Vehiculo;
import com.clwash.modelo.excepciones.DAOException;
import com.clwash.modelo.excepciones.EntidadNoEncontradaException;
import com.clwash.modelo.excepciones.ValidacionException;

/**
 * Caso de uso: <b>Consultar vehículo por patente</b>.
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class BuscarVehiculo {

    /**
     * Ejecuta el caso de uso.
     *
     * @param patente patente a buscar
     * @param dao     DAO de consulta
     * @return el vehículo encontrado (nunca {@code null})
     * @throws ValidacionException          si la patente está vacía o mal formada
     * @throws EntidadNoEncontradaException si no hay un vehículo con esa patente
     * @throws DAOException                 si hay un error de acceso a datos
     */
    public Vehiculo ejecutar(String patente, VehiculoDAO dao)
            throws ValidacionException, DAOException {
        String normalizada = Vehiculo.normalizarPatente(patente);
        ValidadorVehiculo.validarPatente(normalizada);
        Vehiculo v = dao.buscarPorId(normalizada);
        if (v == null) {
            throw new EntidadNoEncontradaException(
                    "No se encontró ningún vehículo con la patente " + normalizada + ".");
        }
        return v;
    }
}
