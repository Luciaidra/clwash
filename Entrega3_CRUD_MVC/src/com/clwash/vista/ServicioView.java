package com.clwash.vista;

import com.clwash.modelo.entidades.Servicio;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Pantalla del catálogo de servicios del lavadero.
 * <p>
 * <b>Prototipo (entrega 1):</b> tiene todos los componentes y la forma de
 * mostrar los datos, pero todavía no está conectada a un controlador.
 * Sigue la misma estructura que {@link VehiculoView} para que, cuando se
 * implemente su CRUD, solo haga falta agregar el controlador.
 * </p>
 * Datos que se piden al usuario: nombre, descripción, precio y duración.
 * El id lo genera la base de datos, por eso no se pide.
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class ServicioView extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String[] COLUMNAS = {"Id", "Nombre", "Descripción", "Precio", "Duración"};
    private static final Color COLOR_PRIMARIO = new Color(0, 105, 160);
    private static final NumberFormat FORMATO_PESOS =
            NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-AR"));

    private final JTextField txtFiltro = new JTextField(18);
    private final JButton btnBuscar = new JButton("Buscar");

    private final DefaultTableModel modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modeloTabla);

    private final JTextField txtNombre = new JTextField(14);
    private final JTextArea txtDescripcion = new JTextArea(3, 14);
    private final JSpinner spnPrecio = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10_000_000.0, 500.0));
    private final JSpinner spnDuracion = new JSpinner(new SpinnerNumberModel(30, 5, 600, 5));

    private final JButton btnNuevo = new JButton("Nuevo");
    private final JButton btnGuardar = new JButton("Guardar");
    private final JButton btnEliminar = new JButton("Eliminar");

    /** Construye la ventana con todos sus componentes. */
    public ServicioView() {
        super("C&L Wash - Catálogo de servicios");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(crearPanelBusqueda(), BorderLayout.NORTH);
        add(crearPanelListado(), BorderLayout.CENTER);
        add(crearPanelFormulario(), BorderLayout.EAST);

        setMinimumSize(new Dimension(900, 460));
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titulo = new JLabel("Servicios   ");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        titulo.setForeground(COLOR_PRIMARIO);
        panel.add(titulo);
        panel.add(new JLabel("Buscar por nombre:"));
        panel.add(txtFiltro);
        panel.add(btnBuscar);
        return panel;
    }

    private JPanel crearPanelListado() {
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(24);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Catálogo"));
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del servicio"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);

        agregarFila(panel, c, 0, "Nombre *", txtNombre);
        agregarFila(panel, c, 1, "Descripción", new JScrollPane(txtDescripcion));
        agregarFila(panel, c, 2, "Precio ($) *", spnPrecio);
        agregarFila(panel, c, 3, "Duración (min) *", spnDuracion);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        btnGuardar.setBackground(COLOR_PRIMARIO);
        btnGuardar.setForeground(Color.WHITE);
        botones.add(btnNuevo);
        botones.add(btnGuardar);
        botones.add(btnEliminar);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        c.insets = new Insets(15, 5, 5, 5);
        panel.add(botones, c);

        c.gridy = 5;
        c.weighty = 1;
        panel.add(new JLabel(), c);
        return panel;
    }

    private void agregarFila(JPanel panel, GridBagConstraints c, int fila,
                             String etiqueta, java.awt.Component campo) {
        c.gridx = 0;
        c.gridy = fila;
        c.weightx = 0;
        panel.add(new JLabel(etiqueta), c);
        c.gridx = 1;
        c.weightx = 1;
        panel.add(campo, c);
    }

    /**
     * Muestra los servicios en la tabla, con el precio en pesos y la
     * duración en minutos.
     *
     * @param servicios servicios a listar
     */
    public void mostrarServicios(List<Servicio> servicios) {
        modeloTabla.setRowCount(0);
        for (Servicio s : servicios) {
            modeloTabla.addRow(new Object[]{
                    s.getId(),
                    s.getNombre(),
                    s.getDescripcion(),
                    FORMATO_PESOS.format(s.getPrecio()),
                    s.getDuracionMinutos() + " min"
            });
        }
    }
}
