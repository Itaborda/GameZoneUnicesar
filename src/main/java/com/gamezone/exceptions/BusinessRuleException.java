package com.gamezone.exceptions;

/**
 * Thrown when an operation violates a business rule, such as insufficient
 * stock, a duplicated customer or a sale without products.
 */
public class BusinessRuleException extends GameZoneException {

    private static final String ERROR_CODE = "GZ-002";

    /**
     * Creates the exception with a message describing the violated rule.
     *
     * @param message the description of the violated business rule
     */
    public BusinessRuleException(String message) {
        super(message, ERROR_CODE);
    }
}