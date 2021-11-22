package com.mindata.superheroes.exceptions;

public abstract class BusinessException extends BaseException {

    /**
     * Creates a BusinessException with message and cause.
     *
     * @param message       the error message
     * @param cause         the error cause
     */
    public BusinessException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a BusinessException with message and cause.
     *
     * @param message       the error message
     */
    public BusinessException(final String message) {
        super(message);
    }

}
