package com.user.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 404 NOT FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자 입니다."),
    INVALID_TOKEN(HttpStatus.NOT_FOUND, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.NOT_FOUND, "만료된 토큰입니다."),

    // 409 CONFLICT
    EMAIL_CONFLICT(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    NICKNAME_CONFLICT(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),;

    private final HttpStatus status;
    private final String message;

}
