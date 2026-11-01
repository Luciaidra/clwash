package com.clwash.modelo.entidades;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Representa una prestación del catálogo del lavadero
 * (por ejemplo "Lavado completo" o "Encerado") con su tarifa y duración.
 * <p>
 * El precio se maneja con {@link BigDecimal} para evitar los errores de
 * redondeo que tienen {@code double} y {@code float} con importes de dinero.
 * </p>
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class Servicio {

    private int id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int duracionMinutos;

    /** Constructor vacío. */
    public Servicio() {
    }

    /**
     * Crea un servicio nuevo (todavía sin id, lo asigna la base de datos).
     *
     * @param nombre          nombre del servicio
     * @param descripcion     detalle de lo que incluye
     * @param precio          tarifa vigente
     * @param duracionMinutos tiempo estimado en minutos
     */
    public Servicio(String nombre, String descripcion, BigDecimal precio, int duracionMinutos) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.duracionMinutos = duracionMinutos;
    }

    /**
     * Crea un servicio con id (por ejemplo, al leerlo de la base).
     *
     * @param id              identificador
     * @param nombre          nombre del servicio
     * @param descripcion     detalle de lo que incluye
     * @param precio          tarifa vigente
     * @param duracionMinutos tiempo estimado en minutos
     */
    public Servicio(int id, String nombre, String descripcion, BigDecimal precio, int duracionMinutos) {
        this(nombre, descripcion, precio, duracionMinutos);
        this.id = id;
    }

    /** @return el identificador */
    public int getId() {
        return id;
    }

    /** @param id nuevo identificador */
    public void setId(int id) {
        this.id = id;
    }

    /** @return el nombre */
    public String getNombre() {
        return nombre;
    }

    /** @param nombre nuevo nombre */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** @return la descripción */
    public String getDescripcion() {
        return descripcion;
    }

    /** @param descripcion nueva descripción */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /** @return el precio */
    public BigDecimal getPrecio() {
        return precio;
    }

    /** @param precio nuevo precio */
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    /** @return la duración estimada en minutos */
    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    /** @param duracionMinutos nueva duración en minutos */
    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    /**
     * Dos servicios son iguales si tienen el mismo id.
     *
     * @param o objeto a comparar
     * @return {@code true} si tienen el mismo id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Servicio)) {
            return false;
        }
        return id == ((Servicio) o).id;
    }

    /** @return hash calculado a partir del id */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /** @return el nombre del servicio (útil para mostrarlo en combos) */
    @Override
    public String toString() {
        return nombre;
    }
}
