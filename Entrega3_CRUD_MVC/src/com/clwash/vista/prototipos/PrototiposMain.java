package com.clwash.vista.prototipos;

import com.clwash.modelo.entidades.Servicio;
import com.clwash.modelo.entidades.TipoVehiculo;
import com.clwash.modelo.entidades.Vehiculo;
import com.clwash.vista.ServicioView;
import com.clwash.vista.VehiculoView;

import javax.swing.SwingUtilities;
import java.math.BigDecimal;
import java.util.List;

/**
 * Lanzador de los prototipos de interfaz (entrega 1).
 * <p>
 * Abre las pantallas de Vehículos y Servicios <b>sin controlador ni base de
 * datos</b>, cargadas con datos de ejemplo, para mostrar qué se le pide al
 * usuario, qué se muestra y cómo se lista cada entidad.
 * Los botones todavía no hacen nada.
 * </p>
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class PrototiposMain {

    /**
     * Muestra ambos prototipos.
     *
     * @param args no se usan
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VehiculoView vehiculos = new VehiculoView();
            vehiculos.mostrarVehiculos(List.of(
                    new Vehiculo("AB123CD", "Toyota", "Corolla", TipoVehiculo.AUTO),
                    new Vehiculo("AE456FG", "Ford", "Ranger", TipoVehiculo.CAMIONETA),
                    new Vehiculo("KLM789", "Volkswagen", "Gol", TipoVehiculo.AUTO),
                    new Vehiculo("A123BCD", "Honda", "CB 190", TipoVehiculo.MOTO)));
            vehiculos.setLocation(40, 40);
            vehiculos.setVisible(true);

            ServicioView servicios = new ServicioView();
            servicios.mostrarServicios(List.of(
                    new Servicio(1, "Lavado exterior", "Carrocería, llantas y secado",
                            new BigDecimal("8000"), 30),
                    new Servicio(2, "Lavado completo", "Exterior + aspirado e interior",
                            new BigDecimal("15000"), 60),
                    new Servicio(3, "Encerado", "Cera protectora",
                            new BigDecimal("12000"), 45)));
            servicios.setLocation(120, 120);
            servicios.setVisible(true);
        });
    }
}
