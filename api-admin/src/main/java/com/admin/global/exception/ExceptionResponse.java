package com.admin.global.exception;

public record ExceptionResponse(
    int status,
    String error,
    String message
) {
}
