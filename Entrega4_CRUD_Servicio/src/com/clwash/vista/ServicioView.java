package com.clwash.vista;

import com.clwash.modelo.entidades.Servicio;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

/**
 * Pantalla del catálogo de servicios (la "V" de MVC).
 * <p>
 * Igual que {@link VehiculoView}, no tiene lógica de negocio ni accede a la
 * base: muestra los datos, expone lo que escribió el usuario y permite que
 * {@link com.clwash.controlador.ServicioController} registre los listeners.
 * </p>
 * Datos que se piden: nombre, descripción, precio y duración.
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

    // Búsqueda
    private final JTextField txtFiltro = new JTextField(18);
    private final JButton btnBuscar = new JButton("Buscar");
    private final JButton btnMostrarTodos = new JButton("Mostrar todos");

    // Listado
    private final DefaultTableModel modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modeloTabla);
    private final JLabel lblCantidad = new JLabel(" ");

    // Formulario
    private final JLabel lblTituloFormulario = new JLabel("Nuevo servicio");
    private final JTextField txtNombre = new JTextField(14);
    private final JTextArea txtDescripcion = new JTextArea(3, 14);
    private final JSpinner spnPrecio =
            new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10_000_000.0, 500.0));
    private final JSpinner spnDuracion = new JSpinner(new SpinnerNumberModel(30, 5, 600, 5));

    // Acciones
    private final JButton btnNuevo = new JButton("Nuevo");
    private final JButton btnGuardar = new JButton("Guardar");
    private final JButton btnEliminar = new JButton("Eliminar");

    /** Id del servicio que se está editando; 0 si es un servicio nuevo. */
    private int idEdicion = 0;

    /** Construye la ventana con todos sus componentes. */
    public ServicioView() {
        super("C&L Wash - Catálogo de servicios");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(crearPanelBusqueda(), BorderLayout.NORTH);
        add(crearPanelListado(), BorderLayout.CENTER);
        add(crearPanelFormulario(), BorderLayout.EAST);

        limpiarFormulario();
        setMinimumSize(new Dimension(900, 460));
        pack();
        setLocationRelativeTo(null);
    }

    // ---------------------------------------------------------------- armado

    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titulo = new JLabel("Servicios   ");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        titulo.setForeground(COLOR_PRIMARIO);
        panel.add(titulo);
        panel.add(new JLabel("Buscar (nombre o descripción):"));
        panel.add(txtFiltro);
        panel.add(btnBuscar);
        panel.add(btnMostrarTodos);
        txtFiltro.setToolTipText("Escriba parte del nombre o la descripción y presione Enter");
        return panel;
    }

    private JPanel crearPanelListado() {
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(24);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Catálogo"));
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(lblCantidad, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del servicio"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        lblTituloFormulario.setFont(lblTituloFormulario.getFont().deriveFont(Font.BOLD, 14f));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(lblTituloFormulario, c);
        c.gridwidth = 1;

        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);

        agregarFila(panel, c, 1, "Nombre *", txtNombre);
        agregarFila(panel, c, 2, "Descripción", new JScrollPane(txtDescripcion));
        agregarFila(panel, c, 3, "Precio ($) *", spnPrecio);
        agregarFila(panel, c, 4, "Duración (min) *", spnDuracion);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        btnGuardar.setBackground(COLOR_PRIMARIO);
        btnGuardar.setForeground(Color.WHITE);
        botones.add(btnNuevo);
        botones.add(btnGuardar);
        botones.add(btnEliminar);
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        c.insets = new Insets(15, 5, 5, 5);
        panel.add(botones, c);

        c.gridy = 6;
        c.weighty = 1;
        panel.add(new JLabel(), c);
        return panel;
    }

    private void agregarFila(JPanel panel, GridBagConstraints c, int fila,
                             String etiqueta, Component campo) {
        c.gridx = 0;
        c.gridy = fila;
        c.weightx = 0;
        panel.add(new JLabel(etiqueta), c);
        c.gridx = 1;
        c.weightx = 1;
        panel.add(campo, c);
    }

    // ----------------------------------------------------- datos del usuario

    /** @return id del servicio en edición, o 0 si es nuevo */
    public int getIdEdicion() {
        return idEdicion;
    }

    /** @return el nombre escrito en el formulario */
    public String getNombre() {
        return txtNombre.getText();
    }

    /** @return la descripción escrita en el formulario */
    public String getDescripcion() {
        return txtDescripcion.getText();
    }

    /**
     * Devuelve el precio cargado, redondeado a 2 decimales.
     * Si el usuario escribió algo que no es un número, devuelve {@code null}.
     *
     * @return el precio, o {@code null} si es inválido
     */
    public BigDecimal getPrecio() {
        try {
            spnPrecio.commitEdit(); // toma lo que se escribió aunque no se haya salido del campo
            double valor = ((Number) spnPrecio.getValue()).doubleValue();
            return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP);
        } catch (ParseException e) {
            return null;
        }
    }

    /** @return la duración en minutos */
    public int getDuracion() {
        try {
            spnDuracion.commitEdit();
        } catch (ParseException e) {
            // se queda con el último valor válido del spinner
        }
        return ((Number) spnDuracion.getValue()).intValue();
    }

    /** @return el texto de búsqueda */
    public String getFiltro() {
        return txtFiltro.getText();
    }

    /** Borra el texto de búsqueda. */
    public void limpiarFiltro() {
        txtFiltro.setText("");
    }

    /**
     * Devuelve el id de la fila seleccionada en la tabla.
     *
     * @return el id, o 0 si no hay ninguna fila seleccionada
     */
    public int getIdSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return 0;
        }
        return (Integer) modeloTabla.getValueAt(tabla.convertRowIndexToModel(fila), 0);
    }

    /** @return {@code true} si se está editando un servicio existente */
    public boolean isModoEdicion() {
        return idEdicion > 0;
    }

    // ------------------------------------------------------- mostrar datos

    /**
     * Muestra los servicios en la tabla, con el precio en pesos y la duración en minutos.
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
        lblCantidad.setText(servicios.size() + " servicio(s)");
    }

    /**
     * Completa el formulario con un servicio existente y lo pone en modo edición.
     *
     * @param s servicio a editar
     */
    public void cargarFormulario(Servicio s) {
        idEdicion = s.getId();
        txtNombre.setText(s.getNombre());
        txtDescripcion.setText(s.getDescripcion() == null ? "" : s.getDescripcion());
        spnPrecio.setValue(s.getPrecio().doubleValue());
        spnDuracion.setValue(s.getDuracionMinutos());
        actualizarModo();
    }

    /** Vacía el formulario y lo deja listo para cargar un servicio nuevo. */
    public void limpiarFormulario() {
        idEdicion = 0;
        txtNombre.setText("");
        txtDescripcion.setText("");
        spnPrecio.setValue(0.0);
        spnDuracion.setValue(30);
        tabla.clearSelection();
        actualizarModo();
        txtNombre.requestFocusInWindow();
    }

    private void actualizarModo() {
        btnEliminar.setEnabled(isModoEdicion());
        lblTituloFormulario.setText(isModoEdicion()
                ? "Editando servicio #" + idEdicion : "Nuevo servicio");
    }

    // ------------------------------------------------------------- mensajes

    /** @param mensaje texto informativo a mostrar */
    public void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "C&L Wash", JOptionPane.INFORMATION_MESSAGE);
    }

    /** @param mensaje advertencia (dato incorrecto que el usuario puede corregir) */
    public void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Revise los datos", JOptionPane.WARNING_MESSAGE);
    }

    /** @param mensaje error técnico (por ejemplo, falla de conexión) */
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Pide confirmación al usuario.
     *
     * @param pregunta texto de la pregunta
     * @return {@code true} si el usuario eligió "Sí"
     */
    public boolean confirmar(String pregunta) {
        return JOptionPane.showConfirmDialog(this, pregunta, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    // ------------------------------------------------------------ listeners

    /** @param l acción del botón Nuevo */
    public void addNuevoListener(ActionListener l) {
        btnNuevo.addActionListener(l);
    }

    /** @param l acción del botón Guardar */
    public void addGuardarListener(ActionListener l) {
        btnGuardar.addActionListener(l);
    }

    /** @param l acción del botón Eliminar */
    public void addEliminarListener(ActionListener l) {
        btnEliminar.addActionListener(l);
    }

    /** @param l acción de buscar (botón Buscar o Enter en el campo de búsqueda) */
    public void addBuscarListener(ActionListener l) {
        btnBuscar.addActionListener(l);
        txtFiltro.addActionListener(l);
    }

    /** @param l acción del botón Mostrar todos */
    public void addMostrarTodosListener(ActionListener l) {
        btnMostrarTodos.addActionListener(l);
    }

    /** @param l se ejecuta cuando cambia la fila seleccionada de la tabla */
    public void addSeleccionListener(ListSelectionListener l) {
        tabla.getSelectionModel().addListSelectionListener(l);
    }
}
