package com.gamezone.exceptions;

/**
 * Thrown when an input value does not meet format, range or type criteria,
 * such as a negative price or an empty required text.
 */
public class InvalidDataException extends GameZoneException {

    private static final String ERROR_CODE = "GZ-003";

    /**
     * Creates the exception and builds a descriptive Spanish message.
     *
     * @param fieldName the name of the invalid field (e.g. "precio")
     * @param problem   the description of the problem (e.g. "debe ser mayor que cero")
     */
    public InvalidDataException(String fieldName, String problem) {
        super("El campo '" + fieldName + "' " + problem, ERROR_CODE);
    }
}