package com.mindata.superheroes.exceptions;

import com.mindata.superheroes.responses.ErrorResponse;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.http.HttpStatus;

public class SuperheroException extends BusinessException {

    private static final long serialVersionUID = 1L;

    private final String error;
    private final String message;
    private final HttpStatus status;

    /**
     * Private constructor. Creates a SuperheroException with error, message and status.
     *
     * @param error         the error code
     * @param message       the error message
     * @param status        the error status
     */
    private SuperheroException(final String error, final String message, final HttpStatus status) {
        super(message);
        this.error = error;
        this.message = message;
        this.status = status;
    }

    /**
     * Private constructor. Creates a SuperheroException with error, message, status and cause.
     *
     * @param error         the error code
     * @param message       the error message
     * @param status        the error status
     * @param cause         the error cause
     */
    private SuperheroException(final String error, final String message, final HttpStatus status, final Throwable cause) {
        super(message, cause);
        this.error = error;
        this.message = message;
        this.status = status;
    }

    /**
     * Creates a SuperheroException.
     *
     * @return a new {@link SuperheroException} with error, message and status.
     */
    public static SuperheroException ofNotFoundSuperhero() {
        return new SuperheroException("superhero_not_found", "Invalid or not found superhero", HttpStatus.NOT_FOUND);
    }

    /**
     * Creates a SuperheroException.
     *
     * @return a new {@link SuperheroException} with error, message and status.
     */
    public static SuperheroException ofBadRequest() {
        return new SuperheroException("superhero_bad_request", "Invalid request", HttpStatus.BAD_REQUEST);
    }

    /**
     * Creates a SuperheroException with error, message, status and cause.
     *
     * @param error         the error code
     * @param message       the error message
     * @param status        the error status
     * @param cause         the error cause
     * @return a new {@link SuperheroException} with error, message, status and cause.
     */
    public static SuperheroException of(final String error, final String message, final HttpStatus status, final Throwable cause) {
        return new SuperheroException(error, message, status, cause);
    }

    public String getError() {
        return error;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public ErrorResponse toErrorResponse() {
        return new ErrorResponse(message, error);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("cause", getCause())
            .append("error", error)
            .append("message", message)
            .append("status", status)
            .toString();
    }
}
