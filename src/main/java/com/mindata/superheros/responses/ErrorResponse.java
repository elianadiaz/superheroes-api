package com.mindata.superheros.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class ErrorResponse {
    private final Throwable cause;
    private final String message;
    private final String code;
    @JsonProperty("status_code")
    private final int statusCode;
}
