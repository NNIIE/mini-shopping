package com.admin.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleGlobalException(
        final Exception ex,
        final HttpServletRequest request
    ) {
        log.error("Runtime exception: {}, URI: {}",
            ex.getMessage(), request.getRequestURI());

        final ExceptionResponse response = new ExceptionResponse(
            INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            ex.getMessage()
        );

        return ResponseEntity
            .status(INTERNAL_SERVER_ERROR)
            .body(response);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse> handleCustomException(final CustomException ex) {
        final ExceptionResponse response = new ExceptionResponse(
            ex.getErrorCode().getStatus().value(),
            ex.getErrorCode().name(),
            ex.getMessage()
        );

        return ResponseEntity
            .status(ex.getErrorCode().getStatus())
            .body(response);
    }

}
