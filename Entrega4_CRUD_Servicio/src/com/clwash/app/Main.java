package com.clwash.app;

import com.clwash.controlador.MenuController;
import com.clwash.modelo.dao.impl.ServicioDAOJDBC;
import com.clwash.modelo.dao.impl.VehiculoDAOJDBC;
import com.clwash.vista.MenuPrincipalView;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Punto de entrada de la aplicación C&amp;L Wash.
 * <p>
 * Crea los DAO (modelo), el menú principal (vista) y su controlador.
 * Desde el menú se abre la gestión de Vehículos y de Servicios.
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
        SwingUtilities.invokeLater(() ->
                new MenuController(new MenuPrincipalView(),
                        new VehiculoDAOJDBC(),
                        new ServicioDAOJDBC()).iniciar());
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
