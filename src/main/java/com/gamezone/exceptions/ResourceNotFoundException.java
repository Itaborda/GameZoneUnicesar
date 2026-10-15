package com.gamezone.exceptions;

import java.util.Set;

/**
 * Thrown when a requested resource (product, customer, seller, sale, etc.)
 * does not exist in the system.
 */
public class ResourceNotFoundException extends GameZoneException {

    private static final String ERROR_CODE = "GZ-001";

    /** Resource types that take the feminine article in Spanish. */
    private static final Set<String> FEMININE_RESOURCES =
            Set.of("venta", "persona", "promoción", "garantía", "devolución");

    /**
     * Creates the exception and builds a descriptive Spanish message.
     *
     * @param resourceType the kind of resource searched (e.g. "producto")
     * @param identifier   the identifier that was searched
     */
    public ResourceNotFoundException(String resourceType, String identifier) {
        super(buildMessage(resourceType, identifier), ERROR_CODE);
    }

    private static String buildMessage(String resourceType, String identifier) {
        String type = (resourceType == null || resourceType.isBlank())
                ? "recurso" : resourceType;
        boolean feminine = FEMININE_RESOURCES.contains(type.toLowerCase());
        String article = feminine ? "La" : "El";
        String ending = feminine ? "encontrada" : "encontrado";
        return article + " " + type + " con identificador " + identifier
                + " no fue " + ending;
    }
}