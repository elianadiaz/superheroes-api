package com.mindata.superheroes.exceptions;

import com.mindata.superheroes.responses.ErrorResponse;
import org.springframework.http.HttpStatus;

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
     * Maps the current Exception to a REST ErrorResponse
     * @return the ErrorResponse representing the current Exception
     */
    public abstract ErrorResponse toErrorResponse();

    /**
     * Get status
     * @return the HttpStatus
     */
    public abstract HttpStatus getStatus();
}
