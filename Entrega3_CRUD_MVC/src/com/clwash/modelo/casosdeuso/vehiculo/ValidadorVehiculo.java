package com.clwash.modelo.casosdeuso.vehiculo;

import com.clwash.modelo.entidades.Vehiculo;
import com.clwash.modelo.excepciones.ValidacionException;

import java.util.regex.Pattern;

/**
 * Reglas de negocio que debe cumplir un {@link Vehiculo} antes de guardarse.
 * <p>
 * Se usa desde los casos de uso de alta y modificación, para no repetir
 * las validaciones en cada uno.
 * </p>
 * Formatos de patente argentinos aceptados:
 * <ul>
 *   <li>{@code ABC123} – autos, formato anterior a 2016</li>
 *   <li>{@code AB123CD} – autos, formato Mercosur</li>
 *   <li>{@code 123ABC} – motos, formato anterior</li>
 *   <li>{@code A123BCD} – motos, formato Mercosur</li>
 * </ul>
 *
 * @author Lucía Mangioni, Catalina Baviello
 */
public final class ValidadorVehiculo {

    private static final Pattern FORMATO_PATENTE = Pattern.compile(
            "^([A-Z]{3}\\d{3}|[A-Z]{2}\\d{3}[A-Z]{2}|\\d{3}[A-Z]{3}|[A-Z]\\d{3}[A-Z]{3})$");

    private static final int LARGO_MAXIMO = 40;

    /** Clase utilitaria: no se instancia. */
    private ValidadorVehiculo() {
    }

    /**
     * Verifica que el vehículo tenga todos los datos y que sean válidos.
     *
     * @param v vehículo a validar
     * @throws ValidacionException con un mensaje que explica el primer error encontrado
     */
    public static void validar(Vehiculo v) throws ValidacionException {
        if (v == null) {
            throw new ValidacionException("No se recibieron los datos del vehículo.");
        }
        validarPatente(v.getPatente());
        validarTexto(v.getMarca(), "marca");
        validarTexto(v.getModelo(), "modelo");
        if (v.getTipo() == null) {
            throw new ValidacionException("Debe seleccionar el tipo de vehículo.");
        }
    }

    /**
     * Verifica que la patente no esté vacía y tenga un formato argentino válido.
     *
     * @param patente patente ya normalizada
     * @throws ValidacionException si está vacía o el formato es incorrecto
     */
    public static void validarPatente(String patente) throws ValidacionException {
        if (patente == null || patente.isEmpty()) {
            throw new ValidacionException("La patente es obligatoria.");
        }
        if (!FORMATO_PATENTE.matcher(patente).matches()) {
            throw new ValidacionException("La patente '" + patente + "' no tiene un formato válido.\n"
                    + "Ejemplos: ABC123, AB123CD (autos) o A123BCD, 123ABC (motos).");
        }
    }

    private static void validarTexto(String valor, String campo) throws ValidacionException {
        if (valor == null || valor.isBlank()) {
            throw new ValidacionException("El campo " + campo + " es obligatorio.");
        }
        if (valor.trim().length() > LARGO_MAXIMO) {
            throw new ValidacionException("El campo " + campo + " no puede superar los "
                    + LARGO_MAXIMO + " caracteres.");
        }
    }
}
