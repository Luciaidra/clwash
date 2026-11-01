package com.clwash.modelo.entidades;

import java.util.Objects;

/**
 * Representa un rodado que ingresa a la playa del lavadero.
 * <p>
 * La patente es el identificador natural del vehículo (clave primaria en la
 * base de datos). Se guarda siempre normalizada: en mayúsculas y sin espacios
 * ni guiones.
 * </p>
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public class Vehiculo {

    private String patente;
    private String marca;
    private String modelo;
    private TipoVehiculo tipo;

    /**
     * Constructor vacío, necesario para crear el objeto y luego
     * completarlo con los setters (por ejemplo, al leer de la base).
     */
    public Vehiculo() {
    }

    /**
     * Crea un vehículo con todos sus datos.
     *
     * @param patente patente del rodado (se normaliza automáticamente)
     * @param marca   marca del fabricante
     * @param modelo  modelo del vehículo
     * @param tipo    tipo de rodado
     */
    public Vehiculo(String patente, String marca, String modelo, TipoVehiculo tipo) {
        setPatente(patente);
        this.marca = marca;
        this.modelo = modelo;
        this.tipo = tipo;
    }

    /**
     * Normaliza una patente: quita espacios y guiones y la pasa a mayúsculas.
     * Por ejemplo {@code "ab 123-cd"} se convierte en {@code "AB123CD"}.
     *
     * @param patente patente tal como la escribió el usuario
     * @return patente normalizada, o {@code null} si se recibió {@code null}
     */
    public static String normalizarPatente(String patente) {
        if (patente == null) {
            return null;
        }
        return patente.replaceAll("[\\s-]", "").toUpperCase();
    }

    /** @return la patente normalizada */
    public String getPatente() {
        return patente;
    }

    /** @param patente nueva patente (se normaliza automáticamente) */
    public void setPatente(String patente) {
        this.patente = normalizarPatente(patente);
    }

    /** @return la marca */
    public String getMarca() {
        return marca;
    }

    /** @param marca nueva marca */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /** @return el modelo */
    public String getModelo() {
        return modelo;
    }

    /** @param modelo nuevo modelo */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /** @return el tipo de vehículo */
    public TipoVehiculo getTipo() {
        return tipo;
    }

    /** @param tipo nuevo tipo de vehículo */
    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }

    /**
     * Dos vehículos son iguales si tienen la misma patente.
     *
     * @param o objeto a comparar
     * @return {@code true} si ambos tienen la misma patente
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vehiculo)) {
            return false;
        }
        Vehiculo otro = (Vehiculo) o;
        return Objects.equals(patente, otro.patente);
    }

    /** @return hash calculado a partir de la patente */
    @Override
    public int hashCode() {
        return Objects.hash(patente);
    }

    /** @return texto del tipo "AB123CD - Toyota Corolla" */
    @Override
    public String toString() {
        return patente + " - " + marca + " " + modelo;
    }
}
