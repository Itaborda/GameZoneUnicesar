package com.gamezone.exceptions;

/**
 * Thrown when reading or writing a file fails: corrupt file, permission
 * denied, full disk, etc. It keeps the original cause for traceability.
 */
public class PersistenceException extends GameZoneException {

    private static final String ERROR_CODE = "GZ-004";

    /**
     * Creates the exception preserving the original cause.
     *
     * @param message the description of the failed operation
     * @param cause   the original exception (usually an IOException)
     */
    public PersistenceException(String message, Throwable cause) {
        super(message, ERROR_CODE);
        initCause(cause);
    }
}