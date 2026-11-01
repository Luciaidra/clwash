package com.clwash.modelo.casosdeuso.servicio;

import com.clwash.modelo.dao.ServicioDAO;
import com.clwash.modelo.entidades.Servicio;
import com.clwash.modelo.excepciones.DAOException;

import java.util.List;

/**
 * Caso de uso: <b>Listar servicios</b>, con filtro opcional por texto.
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class ListarServicios {

    /**
     * Ejecuta el caso de uso.
     *
     * @param filtro texto a buscar en nombre o descripción;
     *               si es {@code null} o vacío se listan todos
     * @param dao    DAO de consulta
     * @return servicios que cumplen el filtro
     * @throws DAOException si hay un error de acceso a datos
     */
    public List<Servicio> ejecutar(String filtro, ServicioDAO dao) throws DAOException {
        if (filtro == null || filtro.isBlank()) {
            return dao.listarTodos();
        }
        return dao.buscarPorTexto(filtro.trim());
    }
}
