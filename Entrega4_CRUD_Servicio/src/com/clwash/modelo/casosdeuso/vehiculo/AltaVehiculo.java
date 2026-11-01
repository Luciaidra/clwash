package com.clwash.modelo.casosdeuso.vehiculo;

import com.clwash.modelo.dao.VehiculoDAO;
import com.clwash.modelo.entidades.Vehiculo;
import com.clwash.modelo.excepciones.DAOException;
import com.clwash.modelo.excepciones.EntidadDuplicadaException;
import com.clwash.modelo.excepciones.ValidacionException;

/**
 * Caso de uso: <b>Registrar vehículo</b>.
 * <p>
 * Valida los datos, controla que la patente no esté repetida y guarda el vehículo.
 * </p>
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class AltaVehiculo {

    /**
     * Ejecuta el caso de uso.
     *
     * @param vehiculo vehículo a registrar
     * @param dao      DAO con el que se persiste (lo provee el controlador)
     * @throws ValidacionException       si algún dato es inválido
     * @throws EntidadDuplicadaException si la patente ya está registrada
     * @throws DAOException              si hay un error de acceso a datos
     */
    public void ejecutar(Vehiculo vehiculo, VehiculoDAO dao)
            throws ValidacionException, DAOException {
        limpiar(vehiculo);
        ValidadorVehiculo.validar(vehiculo);
        if (dao.existePatente(vehiculo.getPatente())) {
            throw new EntidadDuplicadaException(
                    "Ya existe un vehículo con la patente " + vehiculo.getPatente() + ".");
        }
        dao.insertar(vehiculo);
    }

    /** Quita espacios sobrantes de los campos de texto. */
    static void limpiar(Vehiculo v) {
        if (v == null) {
            return;
        }
        if (v.getMarca() != null) {
            v.setMarca(v.getMarca().trim());
        }
        if (v.getModelo() != null) {
            v.setModelo(v.getModelo().trim());
        }
    }
}
