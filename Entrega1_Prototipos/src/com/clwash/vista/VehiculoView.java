package com.clwash.vista;

import com.clwash.modelo.entidades.TipoVehiculo;
import com.clwash.modelo.entidades.Vehiculo;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Pantalla de gestión de vehículos (la "V" de MVC).
 * <p>
 * <b>Prototipo (entrega 1):</b> tiene todos los componentes y la forma de
 * mostrar los datos, pero todavía no está conectada a un controlador ni a la
 * base de datos. La vista no tendrá lógica de negocio: solo expone los datos
 * que escribe el usuario y permite registrar los listeners de los botones.
 * </p>
 * Distribución de la pantalla:
 * <ul>
 *   <li>Arriba: búsqueda por patente, marca o modelo.</li>
 *   <li>Centro: tabla con el listado de vehículos.</li>
 *   <li>Derecha: formulario de alta / modificación.</li>
 * </ul>
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class VehiculoView extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String[] COLUMNAS = {"Patente", "Marca", "Modelo", "Tipo"};
    private static final Color COLOR_PRIMARIO = new Color(0, 105, 160);

    // Búsqueda
    private final JTextField txtFiltro = new JTextField(18);
    private final JButton btnBuscar = new JButton("Buscar");
    private final JButton btnMostrarTodos = new JButton("Mostrar todos");

    // Listado
    private final DefaultTableModel modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false; // la tabla es solo de consulta
        }
    };
    private final JTable tabla = new JTable(modeloTabla);
    private final JLabel lblCantidad = new JLabel(" ");

    // Formulario
    private final JLabel lblTituloFormulario = new JLabel("Nuevo vehículo");
    private final JTextField txtPatente = new JTextField(12);
    private final JTextField txtMarca = new JTextField(12);
    private final JTextField txtModelo = new JTextField(12);
    private final JComboBox<TipoVehiculo> cmbTipo = new JComboBox<>(TipoVehiculo.values());

    // Acciones
    private final JButton btnNuevo = new JButton("Nuevo");
    private final JButton btnGuardar = new JButton("Guardar");
    private final JButton btnEliminar = new JButton("Eliminar");

    private boolean modoEdicion = false;

    /**
     * Construye la ventana con todos sus componentes.
     * No la hace visible: de eso se encarga el controlador.
     */
    public VehiculoView() {
        super("C&L Wash - Gestión de vehículos");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(crearPanelBusqueda(), BorderLayout.NORTH);
        add(crearPanelListado(), BorderLayout.CENTER);
        add(crearPanelFormulario(), BorderLayout.EAST);

        limpiarFormulario();
        setMinimumSize(new Dimension(820, 460));
        pack();
        setLocationRelativeTo(null);
    }

    // ---------------------------------------------------------------- armado

    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titulo = new JLabel("Vehículos   ");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        titulo.setForeground(COLOR_PRIMARIO);
        panel.add(titulo);
        panel.add(new JLabel("Buscar (patente, marca o modelo):"));
        panel.add(txtFiltro);
        panel.add(btnBuscar);
        panel.add(btnMostrarTodos);
        txtFiltro.setToolTipText("Escriba parte de la patente, marca o modelo y presione Enter");
        return panel;
    }

    private JPanel crearPanelListado() {
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(24);
        tabla.setAutoCreateRowSorter(true);
        tabla.getTableHeader().setReorderingAllowed(false);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Listado"));
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(lblCantidad, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del vehículo"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        lblTituloFormulario.setFont(lblTituloFormulario.getFont().deriveFont(Font.BOLD, 14f));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(lblTituloFormulario, c);
        c.gridwidth = 1;

        agregarFila(panel, c, 1, "Patente *", txtPatente);
        agregarFila(panel, c, 2, "Marca *", txtMarca);
        agregarFila(panel, c, 3, "Modelo *", txtModelo);
        agregarFila(panel, c, 4, "Tipo *", cmbTipo);

        JLabel ayuda = new JLabel("<html><small>Ej.: AB123CD, ABC123, A123BCD</small></html>");
        ayuda.setForeground(Color.GRAY);
        c.gridx = 1;
        c.gridy = 5;
        panel.add(ayuda, c);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        btnGuardar.setBackground(COLOR_PRIMARIO);
        btnGuardar.setForeground(Color.WHITE);
        botones.add(btnNuevo);
        botones.add(btnGuardar);
        botones.add(btnEliminar);
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 2;
        c.insets = new Insets(15, 5, 5, 5);
        panel.add(botones, c);

        // Empuja todo hacia arriba
        c.gridy = 7;
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

    // ----------------------------------------------------- datos del usuario

    /** @return la patente escrita en el formulario */
    public String getPatente() {
        return txtPatente.getText();
    }

    /** @return la marca escrita en el formulario */
    public String getMarca() {
        return txtMarca.getText();
    }

    /** @return el modelo escrito en el formulario */
    public String getModelo() {
        return txtModelo.getText();
    }

    /** @return el tipo seleccionado en el combo */
    public TipoVehiculo getTipo() {
        return (TipoVehiculo) cmbTipo.getSelectedItem();
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
     * Devuelve la patente de la fila seleccionada en la tabla.
     *
     * @return la patente, o {@code null} si no hay ninguna fila seleccionada
     */
    public String getPatenteSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return null;
        }
        // Se convierte el índice porque la tabla puede estar ordenada
        int filaModelo = tabla.convertRowIndexToModel(fila);
        return (String) modeloTabla.getValueAt(filaModelo, 0);
    }

    /**
     * @return {@code true} si el formulario está editando un vehículo existente,
     *         {@code false} si está cargando uno nuevo
     */
    public boolean isModoEdicion() {
        return modoEdicion;
    }

    // ------------------------------------------------------- mostrar datos

    /**
     * Carga la tabla con la lista recibida.
     *
     * @param vehiculos vehículos a mostrar
     */
    public void mostrarVehiculos(List<Vehiculo> vehiculos) {
        modeloTabla.setRowCount(0);
        for (Vehiculo v : vehiculos) {
            modeloTabla.addRow(new Object[]{v.getPatente(), v.getMarca(), v.getModelo(), v.getTipo()});
        }
        lblCantidad.setText(vehiculos.size() + " vehículo(s)");
    }

    /**
     * Completa el formulario con un vehículo existente y lo pone en modo edición
     * (la patente queda bloqueada porque es la clave).
     *
     * @param v vehículo a editar
     */
    public void cargarFormulario(Vehiculo v) {
        txtPatente.setText(v.getPatente());
        txtMarca.setText(v.getMarca());
        txtModelo.setText(v.getModelo());
        cmbTipo.setSelectedItem(v.getTipo());
        setModoEdicion(true);
    }

    /** Vacía el formulario y lo deja listo para cargar un vehículo nuevo. */
    public void limpiarFormulario() {
        txtPatente.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        cmbTipo.setSelectedIndex(0);
        tabla.clearSelection();
        setModoEdicion(false);
        txtPatente.requestFocusInWindow();
    }

    private void setModoEdicion(boolean edicion) {
        modoEdicion = edicion;
        txtPatente.setEditable(!edicion);
        btnEliminar.setEnabled(edicion);
        lblTituloFormulario.setText(edicion ? "Editando vehículo" : "Nuevo vehículo");
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
