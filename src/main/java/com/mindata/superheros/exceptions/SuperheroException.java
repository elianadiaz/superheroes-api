package com.mindata.superheros.exceptions;

import org.springframework.http.HttpStatus;

public class SuperheroException extends Exception {
    public String getError() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public HttpStatus getStatus() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
