package com.mindata.superheroes.exceptions;

import com.mindata.superheroes.responses.ErrorResponse;

public abstract class BaseException extends Exception {

    /**
     * Creates a BaseException with message and cause.
     *
     * @param message       the error message
     * @param cause         the error cause
     */
    public BaseException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a BaseException with message and cause.
     *
     * @param message       the error message
     */
    public BaseException(final String message) {
        super(message);
    }

    /**
     * Maps the current Business Exception to a REST ErrorResponse
     * @return the ErrorResponse representing the current BusinessException
     */
    public abstract ErrorResponse toErrorResponse();
}
