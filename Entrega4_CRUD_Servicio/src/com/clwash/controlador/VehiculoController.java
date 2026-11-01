package com.clwash.controlador;

import com.clwash.modelo.casosdeuso.vehiculo.AltaVehiculo;
import com.clwash.modelo.casosdeuso.vehiculo.BuscarVehiculo;
import com.clwash.modelo.casosdeuso.vehiculo.EliminarVehiculo;
import com.clwash.modelo.casosdeuso.vehiculo.ListarVehiculos;
import com.clwash.modelo.casosdeuso.vehiculo.ModificarVehiculo;
import com.clwash.modelo.dao.VehiculoDAO;
import com.clwash.modelo.entidades.Vehiculo;
import com.clwash.modelo.excepciones.DAOException;
import com.clwash.modelo.excepciones.EntidadDuplicadaException;
import com.clwash.modelo.excepciones.EntidadNoEncontradaException;
import com.clwash.modelo.excepciones.ValidacionException;
import com.clwash.vista.VehiculoView;

/**
 * Controlador de la gestión de vehículos (la "C" de MVC).
 * <p>
 * Es el intermediario entre la vista y el modelo:
 * </p>
 * <ol>
 *   <li>Escucha los eventos de {@link VehiculoView} (botones, selección de la tabla).</li>
 *   <li>Toma los datos de la vista y arma el {@link Vehiculo}.</li>
 *   <li>Llama al caso de uso que corresponde, pasándole el {@link VehiculoDAO}.</li>
 *   <li>Actualiza la vista con el resultado o muestra el error.</li>
 * </ol>
 * <p>
 * Aquí se <b>resuelven</b> las excepciones del modelo: cada tipo se muestra
 * con un mensaje y un ícono acorde (advertencia para errores del usuario,
 * error para fallas técnicas), y el programa sigue funcionando.
 * </p>
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class VehiculoController {

    private final VehiculoView vista;
    private final VehiculoDAO dao;

    // Un objeto por caso de uso
    private final AltaVehiculo altaVehiculo = new AltaVehiculo();
    private final ModificarVehiculo modificarVehiculo = new ModificarVehiculo();
    private final EliminarVehiculo eliminarVehiculo = new EliminarVehiculo();
    private final BuscarVehiculo buscarVehiculo = new BuscarVehiculo();
    private final ListarVehiculos listarVehiculos = new ListarVehiculos();

    /**
     * Crea el controlador y registra los listeners en la vista.
     *
     * @param vista vista que maneja este controlador
     * @param dao   DAO que se pasa a los casos de uso
     */
    public VehiculoController(VehiculoView vista, VehiculoDAO dao) {
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
            // Evita procesar dos veces el mismo clic
            if (!e.getValueIsAdjusting()) {
                seleccionar();
            }
        });
    }

    // --------------------------------------------------------------- acciones

    /**
     * Guarda el vehículo del formulario: si es nuevo hace el alta,
     * si se está editando hace la modificación.
     */
    void guardar() {
        Vehiculo v = new Vehiculo(vista.getPatente(), vista.getMarca(),
                vista.getModelo(), vista.getTipo());
        boolean esModificacion = vista.isModoEdicion();
        try {
            if (esModificacion) {
                modificarVehiculo.ejecutar(v, dao);
                vista.mostrarInfo("Vehículo " + v.getPatente() + " modificado correctamente.");
            } else {
                altaVehiculo.ejecutar(v, dao);
                vista.mostrarInfo("Vehículo " + v.getPatente() + " registrado correctamente.");
            }
            vista.limpiarFormulario();
            listar(vista.getFiltro());
        } catch (ValidacionException | EntidadDuplicadaException | EntidadNoEncontradaException e) {
            // Errores que el usuario puede corregir: no se pierde lo que escribió
            vista.mostrarAdvertencia(e.getMessage());
        } catch (DAOException e) {
            manejarErrorTecnico(e);
        }
    }

    /** Elimina el vehículo seleccionado, previa confirmación. */
    void eliminar() {
        String patente = vista.getPatente();
        if (!vista.confirmar("¿Seguro que desea eliminar el vehículo " + patente + "?")) {
            return;
        }
        try {
            eliminarVehiculo.ejecutar(patente, dao);
            vista.mostrarInfo("Vehículo " + patente + " eliminado.");
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
            vista.mostrarVehiculos(listarVehiculos.ejecutar(filtro, dao));
        } catch (DAOException e) {
            manejarErrorTecnico(e);
        }
    }

    /** Al seleccionar una fila, trae el vehículo y lo carga en el formulario. */
    void seleccionar() {
        String patente = vista.getPatenteSeleccionada();
        if (patente == null) {
            return;
        }
        try {
            vista.cargarFormulario(buscarVehiculo.ejecutar(patente, dao));
        } catch (ValidacionException | EntidadNoEncontradaException e) {
            // Puede pasar si otro usuario lo borró: se avisa y se refresca
            vista.mostrarAdvertencia(e.getMessage());
            listar(vista.getFiltro());
        } catch (DAOException e) {
            manejarErrorTecnico(e);
        }
    }

    /**
     * Muestra un error técnico al usuario y deja el detalle en la consola
     * para el desarrollador.
     */
    private void manejarErrorTecnico(DAOException e) {
        System.err.println("[C&L Wash] " + e.getMessage());
        if (e.getCause() != null) {
            System.err.println("  Causa: " + e.getCause());
        }
        vista.mostrarError(e.getMessage());
    }
}
