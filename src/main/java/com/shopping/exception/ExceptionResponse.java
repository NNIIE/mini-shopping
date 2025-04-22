package com.shopping.exception;

public record ExceptionResponse(
    int status,
    String error,
    String message
) {

}
