package com.clwash.modelo.entidades;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Orden de trabajo del lavadero: es la entidad que <b>combina</b> un
 * {@link Vehiculo} con un {@link Servicio}.
 * <p>
 * Se incluye en esta etapa para dejar planteado el modelo completo;
 * su DAO y su CRUD se desarrollan en entregas posteriores.
 * </p>
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class OrdenLavado {

    private int id;
    private LocalDateTime fechaHora;
    private EstadoOrden estado;
    private BigDecimal total;
    private String observaciones;
    private Vehiculo vehiculo;
    private Servicio servicio;

    /** Constructor vacío. */
    public OrdenLavado() {
    }

    /**
     * Crea una orden nueva en estado {@link EstadoOrden#PENDIENTE},
     * con la fecha/hora actual y el total tomado del precio del servicio.
     *
     * @param vehiculo      vehículo a lavar
     * @param servicio      servicio contratado
     * @param observaciones notas del operario (puede ser vacío)
     */
    public OrdenLavado(Vehiculo vehiculo, Servicio servicio, String observaciones) {
        this.vehiculo = vehiculo;
        this.servicio = servicio;
        this.observaciones = observaciones;
        this.fechaHora = LocalDateTime.now();
        this.estado = EstadoOrden.PENDIENTE;
        this.total = servicio.getPrecio();
    }

    /** @return el identificador */
    public int getId() {
        return id;
    }

    /** @param id nuevo identificador */
    public void setId(int id) {
        this.id = id;
    }

    /** @return fecha y hora de ingreso */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /** @param fechaHora nueva fecha y hora */
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    /** @return estado actual de la orden */
    public EstadoOrden getEstado() {
        return estado;
    }

    /** @param estado nuevo estado */
    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    /** @return importe total a cobrar */
    public BigDecimal getTotal() {
        return total;
    }

    /** @param total nuevo total */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /** @return observaciones de la orden */
    public String getObservaciones() {
        return observaciones;
    }

    /** @param observaciones nuevas observaciones */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /** @return el vehículo asociado */
    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    /** @param vehiculo nuevo vehículo asociado */
    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    /** @return el servicio contratado */
    public Servicio getServicio() {
        return servicio;
    }

    /** @param servicio nuevo servicio contratado */
    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }
}
