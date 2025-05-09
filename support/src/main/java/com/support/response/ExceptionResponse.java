package com.support.response;

public record ExceptionResponse(
    int status,
    String error,
    String message
) {
}
