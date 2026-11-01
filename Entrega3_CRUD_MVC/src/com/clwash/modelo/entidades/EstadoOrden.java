package com.clwash.modelo.entidades;

/**
 * Estados por los que pasa una orden de lavado.
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public enum EstadoOrden {
    /** El vehículo ingresó y espera ser atendido. */
    PENDIENTE,
    /** El vehículo está siendo lavado. */
    EN_PROCESO,
    /** El trabajo terminó y espera ser retirado/cobrado. */
    FINALIZADA,
    /** La orden se canceló. */
    CANCELADA
}
