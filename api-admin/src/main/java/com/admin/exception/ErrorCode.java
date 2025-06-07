package com.admin.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(1000, "Server error"),

    USER_NOT_FOUND(2001, "존재하지 않는 관리자 입니다."),
    EMAIL_CONFLICT(3001, "이미 사용 중인 이메일입니다."),

    BRAND_NAME_CONFLICT(4001, "이미 사용 중인 브랜드 이름입니다."),
    BRAND_NOT_FOUND(4002, "존재하지 않는 브랜드입니다.");

    private final int code;
    private final String message;

    ErrorCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

}
