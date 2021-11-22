package com.mindata.superheroes.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class ErrorResponse {
    private final String message;
    private final String code;
}
