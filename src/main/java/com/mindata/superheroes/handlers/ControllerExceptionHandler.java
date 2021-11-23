package com.mindata.superheroes.handlers;

import com.mindata.superheroes.exceptions.BaseException;
import com.mindata.superheroes.responses.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> businessException(final BaseException ex) {
        return new ResponseEntity<>(ex.toErrorResponse(), ex.getStatus());
    }
}
