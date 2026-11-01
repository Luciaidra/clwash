package com.clwash.vista;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

/**
 * Ventana principal del sistema: desde acá se abre la gestión de cada entidad.
 * <p>
 * Igual que el resto de las vistas, no decide qué pasa al tocar un botón:
 * eso lo hace {@link com.clwash.controlador.MenuController}.
 * </p>
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class MenuPrincipalView extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final Color COLOR_PRIMARIO = new Color(0, 105, 160);

    private final JButton btnVehiculos = new JButton("Vehículos");
    private final JButton btnServicios = new JButton("Servicios");
    private final JButton btnOrdenes = new JButton("Órdenes (próximamente)");

    /** Construye la ventana del menú. Al cerrarla termina el programa. */
    public MenuPrincipalView() {
        super("C&L Wash");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 15));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titulo = new JLabel("C&L Wash", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 26f));
        titulo.setForeground(COLOR_PRIMARIO);
        JLabel subtitulo = new JLabel("Sistema de gestión del lavadero", SwingConstants.CENTER);

        JPanel encabezado = new JPanel(new GridLayout(2, 1));
        encabezado.add(titulo);
        encabezado.add(subtitulo);
        add(encabezado, BorderLayout.NORTH);

        JPanel botones = new JPanel(new GridLayout(3, 1, 0, 10));
        for (JButton b : new JButton[]{btnVehiculos, btnServicios, btnOrdenes}) {
            b.setFont(b.getFont().deriveFont(15f));
            b.setPreferredSize(new Dimension(260, 42));
            botones.add(b);
        }
        btnOrdenes.setEnabled(false);
        add(botones, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    /** @param l acción del botón Vehículos */
    public void addVehiculosListener(ActionListener l) {
        btnVehiculos.addActionListener(l);
    }

    /** @param l acción del botón Servicios */
    public void addServiciosListener(ActionListener l) {
        btnServicios.addActionListener(l);
    }
}
