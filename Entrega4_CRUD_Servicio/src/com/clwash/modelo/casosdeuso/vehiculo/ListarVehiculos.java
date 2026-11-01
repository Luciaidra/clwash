package com.clwash.modelo.casosdeuso.vehiculo;

import com.clwash.modelo.dao.VehiculoDAO;
import com.clwash.modelo.entidades.Vehiculo;
import com.clwash.modelo.excepciones.DAOException;

import java.util.List;

/**
 * Caso de uso: <b>Listar vehículos</b>, con filtro opcional por texto.
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class ListarVehiculos {

    /**
     * Ejecuta el caso de uso.
     *
     * @param filtro texto a buscar en patente, marca o modelo;
     *               si es {@code null} o vacío se listan todos
     * @param dao    DAO de consulta
     * @return vehículos que cumplen el filtro
     * @throws DAOException si hay un error de acceso a datos
     */
    public List<Vehiculo> ejecutar(String filtro, VehiculoDAO dao) throws DAOException {
        if (filtro == null || filtro.isBlank()) {
            return dao.listarTodos();
        }
        return dao.buscarPorTexto(filtro.trim());
    }
}
