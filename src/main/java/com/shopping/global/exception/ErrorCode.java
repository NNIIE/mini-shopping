package com.shopping.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 404 NOT FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),

    // 409 CONFLICT
    EMAIL_CONFLICT(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    NICKNAME_CONFLICT(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다.");

    private final HttpStatus status;
    private final String message;

}
