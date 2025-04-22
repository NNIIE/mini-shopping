package com.shopping.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 409 CONFLICT
    EMAIL_CONFLICT(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    NICKNAME_CONFLICT(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다.");

    private final HttpStatus status;
    private final String message;

}
