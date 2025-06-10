package com.user.exception;

import com.support.response.ExceptionResponse;
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
        log.error("Server Error: {}, URI: {}",
            ex.getMessage(), request.getRequestURI());

        final ExceptionResponse response = new ExceptionResponse(
            ErrorCode.INTERNAL_SERVER_ERROR.getCode()
        );

        return ResponseEntity
            .status(INTERNAL_SERVER_ERROR)
            .body(response);
    }

    /**
     * BusinessException이 발생했을 때 예외 응답을 반환합니다.
     *
     * @param ex 처리할 BusinessException 인스턴스
     * @param request 현재 HTTP 요청 정보
     * @return 예외 코드가 포함된 ExceptionResponse와 함께 500 Internal Server Error 응답을 반환합니다.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponse> handleBusinessException(
        final BusinessException ex,
        final HttpServletRequest request
    ) {
        final ExceptionResponse response = new ExceptionResponse(
            ex.getErrorCode().getCode()
        );

        return ResponseEntity
            .status(INTERNAL_SERVER_ERROR)
            .body(response);
    }

}

