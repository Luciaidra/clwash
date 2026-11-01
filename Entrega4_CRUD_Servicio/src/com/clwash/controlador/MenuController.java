package com.clwash.controlador;

import com.clwash.modelo.dao.ServicioDAO;
import com.clwash.modelo.dao.VehiculoDAO;
import com.clwash.vista.MenuPrincipalView;
import com.clwash.vista.ServicioView;
import com.clwash.vista.VehiculoView;

import javax.swing.JFrame;

/**
 * Controlador del menú principal: abre la pantalla de cada entidad.
 * <p>
 * Si la ventana de una entidad ya está abierta, no crea otra:
 * la trae al frente.
 * </p>
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class MenuController {

    private final MenuPrincipalView vista;
    private final VehiculoDAO vehiculoDAO;
    private final ServicioDAO servicioDAO;

    private VehiculoView ventanaVehiculos;
    private ServicioView ventanaServicios;

    /**
     * Crea el controlador del menú.
     *
     * @param vista       ventana del menú
     * @param vehiculoDAO DAO que usará la gestión de vehículos
     * @param servicioDAO DAO que usará la gestión de servicios
     */
    public MenuController(MenuPrincipalView vista, VehiculoDAO vehiculoDAO, ServicioDAO servicioDAO) {
        this.vista = vista;
        this.vehiculoDAO = vehiculoDAO;
        this.servicioDAO = servicioDAO;
        vista.addVehiculosListener(e -> abrirVehiculos());
        vista.addServiciosListener(e -> abrirServicios());
    }

    /** Muestra el menú principal. */
    public void iniciar() {
        vista.setVisible(true);
    }

    private void abrirVehiculos() {
        if (estaAbierta(ventanaVehiculos)) {
            ventanaVehiculos.toFront();
            return;
        }
        ventanaVehiculos = new VehiculoView();
        new VehiculoController(ventanaVehiculos, vehiculoDAO).iniciar();
    }

    private void abrirServicios() {
        if (estaAbierta(ventanaServicios)) {
            ventanaServicios.toFront();
            return;
        }
        ventanaServicios = new ServicioView();
        new ServicioController(ventanaServicios, servicioDAO).iniciar();
    }

    /** Una ventana cerrada con DISPOSE deja de ser "displayable". */
    private boolean estaAbierta(JFrame ventana) {
        return ventana != null && ventana.isDisplayable();
    }
}
