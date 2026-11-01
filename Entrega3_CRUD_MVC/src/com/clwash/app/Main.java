package com.clwash.app;

import com.clwash.controlador.VehiculoController;
import com.clwash.modelo.dao.VehiculoDAO;
import com.clwash.modelo.dao.impl.VehiculoDAOJDBC;
import com.clwash.vista.VehiculoView;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Punto de entrada de la aplicación C&amp;L Wash (entrega 3: CRUD de vehículos).
 * <p>
 * Arma las tres piezas de MVC y las conecta: crea el DAO (modelo), la vista
 * y el controlador que los une.
 * </p>
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class Main {

    /**
     * Inicia la aplicación.
     *
     * @param args no se usan
     */
    public static void main(String[] args) {
        aplicarLookAndFeel();
        // Swing debe manejarse desde el hilo de eventos (EDT)
        SwingUtilities.invokeLater(() -> {
            VehiculoDAO dao = new VehiculoDAOJDBC();
            VehiculoView vista = new VehiculoView();
            new VehiculoController(vista, dao).iniciar();
        });
    }

    /** Usa el look and feel Nimbus si está disponible; si no, el por defecto. */
    static void aplicarLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
        } catch (Exception e) {
            // No es un error grave: se sigue con el aspecto por defecto
            System.err.println("No se pudo aplicar Nimbus: " + e.getMessage());
        }
    }
}
