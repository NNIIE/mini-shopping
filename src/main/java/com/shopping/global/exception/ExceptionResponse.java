package com.shopping.global.exception;

public record ExceptionResponse(
    int status,
    String error,
    String message
) {

}
