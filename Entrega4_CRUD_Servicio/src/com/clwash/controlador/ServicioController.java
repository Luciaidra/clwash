package com.clwash.controlador;

import com.clwash.modelo.casosdeuso.servicio.AltaServicio;
import com.clwash.modelo.casosdeuso.servicio.BuscarServicio;
import com.clwash.modelo.casosdeuso.servicio.EliminarServicio;
import com.clwash.modelo.casosdeuso.servicio.ListarServicios;
import com.clwash.modelo.casosdeuso.servicio.ModificarServicio;
import com.clwash.modelo.dao.ServicioDAO;
import com.clwash.modelo.entidades.Servicio;
import com.clwash.modelo.excepciones.DAOException;
import com.clwash.modelo.excepciones.EntidadDuplicadaException;
import com.clwash.modelo.excepciones.EntidadNoEncontradaException;
import com.clwash.modelo.excepciones.ValidacionException;
import com.clwash.vista.ServicioView;

/**
 * Controlador de la gestión del catálogo de servicios (la "C" de MVC).
 * <p>
 * Funciona igual que {@link VehiculoController}: escucha los eventos de
 * {@link ServicioView}, arma el {@link Servicio} con los datos de la vista,
 * llama al caso de uso correspondiente pasándole el {@link ServicioDAO}
 * y muestra el resultado o el error.
 * </p>
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class ServicioController {

    private final ServicioView vista;
    private final ServicioDAO dao;

    private final AltaServicio altaServicio = new AltaServicio();
    private final ModificarServicio modificarServicio = new ModificarServicio();
    private final EliminarServicio eliminarServicio = new EliminarServicio();
    private final BuscarServicio buscarServicio = new BuscarServicio();
    private final ListarServicios listarServicios = new ListarServicios();

    /**
     * Crea el controlador y registra los listeners en la vista.
     *
     * @param vista vista que maneja este controlador
     * @param dao   DAO que se pasa a los casos de uso
     */
    public ServicioController(ServicioView vista, ServicioDAO dao) {
        this.vista = vista;
        this.dao = dao;
        registrarEventos();
    }

    /** Carga el listado inicial y muestra la ventana. */
    public void iniciar() {
        listar(null);
        vista.setVisible(true);
    }

    private void registrarEventos() {
        vista.addNuevoListener(e -> vista.limpiarFormulario());
        vista.addGuardarListener(e -> guardar());
        vista.addEliminarListener(e -> eliminar());
        vista.addBuscarListener(e -> listar(vista.getFiltro()));
        vista.addMostrarTodosListener(e -> {
            vista.limpiarFiltro();
            listar(null);
        });
        vista.addSeleccionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionar();
            }
        });
    }

    // --------------------------------------------------------------- acciones

    /** Guarda el servicio del formulario: alta si es nuevo, modificación si se está editando. */
    void guardar() {
        Servicio s = new Servicio(vista.getNombre(), vista.getDescripcion(),
                vista.getPrecio(), vista.getDuracion());
        try {
            if (vista.isModoEdicion()) {
                s.setId(vista.getIdEdicion());
                modificarServicio.ejecutar(s, dao);
                vista.mostrarInfo("Servicio '" + s.getNombre() + "' modificado correctamente.");
            } else {
                altaServicio.ejecutar(s, dao);
                vista.mostrarInfo("Servicio '" + s.getNombre() + "' registrado con el id " + s.getId() + ".");
            }
            vista.limpiarFormulario();
            listar(vista.getFiltro());
        } catch (ValidacionException | EntidadDuplicadaException | EntidadNoEncontradaException e) {
            vista.mostrarAdvertencia(e.getMessage());
        } catch (DAOException e) {
            manejarErrorTecnico(e);
        }
    }

    /** Elimina el servicio que se está editando, previa confirmación. */
    void eliminar() {
        int id = vista.getIdEdicion();
        if (!vista.confirmar("¿Seguro que desea eliminar el servicio '" + vista.getNombre() + "'?")) {
            return;
        }
        try {
            eliminarServicio.ejecutar(id, dao);
            vista.mostrarInfo("Servicio eliminado.");
            vista.limpiarFormulario();
            listar(vista.getFiltro());
        } catch (ValidacionException | EntidadNoEncontradaException e) {
            vista.mostrarAdvertencia(e.getMessage());
            listar(vista.getFiltro());
        } catch (DAOException e) {
            manejarErrorTecnico(e);
        }
    }

    /**
     * Carga la tabla aplicando el filtro.
     *
     * @param filtro texto a buscar, o {@code null} para listar todos
     */
    void listar(String filtro) {
        try {
            vista.mostrarServicios(listarServicios.ejecutar(filtro, dao));
        } catch (DAOException e) {
            manejarErrorTecnico(e);
        }
    }

    /** Al seleccionar una fila, trae el servicio y lo carga en el formulario. */
    void seleccionar() {
        int id = vista.getIdSeleccionado();
        if (id <= 0) {
            return;
        }
        try {
            vista.cargarFormulario(buscarServicio.ejecutar(id, dao));
        } catch (ValidacionException | EntidadNoEncontradaException e) {
            vista.mostrarAdvertencia(e.getMessage());
            listar(vista.getFiltro());
        } catch (DAOException e) {
            manejarErrorTecnico(e);
        }
    }

    /** Muestra un error técnico al usuario y deja el detalle en la consola. */
    private void manejarErrorTecnico(DAOException e) {
        System.err.println("[C&L Wash] " + e.getMessage());
        if (e.getCause() != null) {
            System.err.println("  Causa: " + e.getCause());
        }
        vista.mostrarError(e.getMessage());
    }
}
