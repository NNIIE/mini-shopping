package com.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(1000, "Server error"),

    USER_NOT_FOUND(2001, "User not found"),

    INVALID_TOKEN(3001, "Invalid token"),
    EXPIRED_TOKEN(3002, "Expired token"),

    EMAIL_CONFLICT(4001, "Email already in use"),
    NICKNAME_CONFLICT(4002, "Nickname already in use"),

    PRODUCT_NOT_FOUND(5001, "Product not found"),
    INSUFFICIENT_STOCK(5002, "Insufficient stock"),;

    private final int code;
    private final String message;

    /**
     * 각 에러 코드 상수에 대한 코드와 메시지를 초기화합니다.
     *
     * @param code 에러 코드 값
     * @param message 에러 메시지
     */
    ErrorCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

}

