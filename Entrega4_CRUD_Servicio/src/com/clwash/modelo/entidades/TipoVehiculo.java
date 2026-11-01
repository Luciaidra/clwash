package com.clwash.modelo.entidades;

/**
 * Tipos de rodado que atiende el lavadero.
 * <p>
 * Se usa un enum para que el tipo solo pueda tomar valores válidos
 * y para poder cargarlo directamente en un {@code JComboBox} de la vista.
 * </p>
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public enum TipoVehiculo {
    /** Auto de pasajeros (sedán, hatchback, etc.). */
    AUTO("Auto"),
    /** Camioneta o pick-up. */
    CAMIONETA("Camioneta"),
    /** Vehículo utilitario deportivo. */
    SUV("SUV"),
    /** Furgón o utilitario de carga. */
    UTILITARIO("Utilitario"),
    /** Motocicleta. */
    MOTO("Moto");

    private final String descripcion;

    TipoVehiculo(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve el nombre legible del tipo, pensado para mostrar en pantalla.
     *
     * @return descripción del tipo de vehículo
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Devuelve la descripción, de modo que los combos muestren "Camioneta"
     * en lugar de "CAMIONETA".
     *
     * @return descripción legible
     */
    @Override
    public String toString() {
        return descripcion;
    }
}
