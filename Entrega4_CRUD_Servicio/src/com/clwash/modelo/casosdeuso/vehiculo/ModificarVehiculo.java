package com.clwash.modelo.casosdeuso.vehiculo;

import com.clwash.modelo.dao.VehiculoDAO;
import com.clwash.modelo.entidades.Vehiculo;
import com.clwash.modelo.excepciones.DAOException;
import com.clwash.modelo.excepciones.EntidadNoEncontradaException;
import com.clwash.modelo.excepciones.ValidacionException;

/**
 * Caso de uso: <b>Modificar vehículo</b>.
 * <p>
 * Permite cambiar marca, modelo y tipo. La patente identifica al vehículo
 * y no se modifica.
 * </p>
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class ModificarVehiculo {

    /**
     * Ejecuta el caso de uso.
     *
     * @param vehiculo vehículo con los datos nuevos
     * @param dao      DAO con el que se persiste
     * @throws ValidacionException          si algún dato es inválido
     * @throws EntidadNoEncontradaException si el vehículo no existe
     * @throws DAOException                 si hay un error de acceso a datos
     */
    public void ejecutar(Vehiculo vehiculo, VehiculoDAO dao)
            throws ValidacionException, DAOException {
        AltaVehiculo.limpiar(vehiculo);
        ValidadorVehiculo.validar(vehiculo);
        dao.modificar(vehiculo);
    }
}
