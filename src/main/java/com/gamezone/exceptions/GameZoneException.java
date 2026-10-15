package com.gamezone.exceptions;

/**
 * Root of the GameZone exception hierarchy.
 * <p>
 * This class is abstract because a generic GameZone error must always be
 * expressed through one of its specialized subclasses. It extends
 * {@link RuntimeException}, so method signatures in the service and
 * repository layers do not need {@code throws} clauses.
 * </p>
 */
public abstract class GameZoneException extends RuntimeException {

    /** Error code used when no specific code is provided. */
    private static final String DEFAULT_ERROR_CODE = "GZ-000";

    private final String errorCode;

    /**
     * Creates an exception with a message and the default error code.
     *
     * @param message the detail message
     */
    public GameZoneException(String message) {
        this(message, DEFAULT_ERROR_CODE);
    }

    /**
     * Creates an exception with a message and a specific error code.
     *
     * @param message   the detail message
     * @param errorCode the code that identifies the kind of error
     */
    public GameZoneException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Returns the error code of this exception.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }
}