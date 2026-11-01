package com.clwash.modelo.casosdeuso.servicio;

import com.clwash.modelo.entidades.Servicio;
import com.clwash.modelo.excepciones.ValidacionException;

import java.math.BigDecimal;

/**
 * Reglas de negocio que debe cumplir un {@link Servicio} antes de guardarse.
 * <ul>
 *   <li>Nombre obligatorio, hasta 60 caracteres.</li>
 *   <li>Descripción opcional, hasta 255 caracteres.</li>
 *   <li>Precio mayor que cero.</li>
 *   <li>Duración entre 5 y 600 minutos.</li>
 * </ul>
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public final class ValidadorServicio {

    private static final int LARGO_NOMBRE = 60;
    private static final int LARGO_DESCRIPCION = 255;
    private static final int DURACION_MINIMA = 5;
    private static final int DURACION_MAXIMA = 600;

    /** Clase utilitaria: no se instancia. */
    private ValidadorServicio() {
    }

    /**
     * Verifica que el servicio tenga datos válidos.
     *
     * @param s servicio a validar
     * @throws ValidacionException con un mensaje que explica el primer error encontrado
     */
    public static void validar(Servicio s) throws ValidacionException {
        if (s == null) {
            throw new ValidacionException("No se recibieron los datos del servicio.");
        }
        if (s.getNombre() == null || s.getNombre().isBlank()) {
            throw new ValidacionException("El nombre del servicio es obligatorio.");
        }
        if (s.getNombre().length() > LARGO_NOMBRE) {
            throw new ValidacionException("El nombre no puede superar los " + LARGO_NOMBRE + " caracteres.");
        }
        if (s.getDescripcion() != null && s.getDescripcion().length() > LARGO_DESCRIPCION) {
            throw new ValidacionException("La descripción no puede superar los "
                    + LARGO_DESCRIPCION + " caracteres.");
        }
        if (s.getPrecio() == null || s.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidacionException("El precio debe ser mayor que cero.");
        }
        if (s.getDuracionMinutos() < DURACION_MINIMA || s.getDuracionMinutos() > DURACION_MAXIMA) {
            throw new ValidacionException("La duración debe estar entre " + DURACION_MINIMA
                    + " y " + DURACION_MAXIMA + " minutos.");
        }
    }

    /**
     * Quita espacios sobrantes; una descripción vacía se guarda como {@code null}.
     *
     * @param s servicio a limpiar
     */
    static void limpiar(Servicio s) {
        if (s == null) {
            return;
        }
        if (s.getNombre() != null) {
            s.setNombre(s.getNombre().trim());
        }
        if (s.getDescripcion() != null) {
            String d = s.getDescripcion().trim();
            s.setDescripcion(d.isEmpty() ? null : d);
        }
    }
}
