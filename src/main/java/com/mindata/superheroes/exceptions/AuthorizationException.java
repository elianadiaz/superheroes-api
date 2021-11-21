package com.mindata.superheroes.exceptions;

import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.http.HttpStatus;

@Getter
public class AuthorizationException extends Exception {

    private static final long serialVersionUID = 1L;

    private final String error;
    private final String message;
    private final int status;

    /**
     * Private constructor. Creates a SuperheroException with error, message and status.
     *
     * @param error         the error code
     * @param message       the error message
     * @param status        the error status
     */
    private AuthorizationException(final String error, final String message, final int status) {
        super(message);
        this.error = error;
        this.message = message;
        this.status = status;
    }

    /**
     * Creates a SuperheroException.
     *
     * @return a new {@link AuthorizationException} with error, message and status.
     */
    public static AuthorizationException ofUnauthorized() {
        return new AuthorizationException("unauthorized_user", "Invalid token", HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Creates a SuperheroException.
     *
     * @return a new {@link AuthorizationException} with error, message and status.
     */
    public static AuthorizationException ofForbidden() {
        return new AuthorizationException("forbidden_user", "Invalid user", HttpStatus.FORBIDDEN.value());
    }

    /**
     * Creates a SuperheroException.
     *
     * @return a new {@link AuthorizationException} with error, message and status.
     */
    public static AuthorizationException ofMissingToken() {
        return new AuthorizationException("missing_token", "Missing token", HttpStatus.UNAUTHORIZED.value());
    }

    @Override
    public Throwable getCause() {
        return getCause();
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
