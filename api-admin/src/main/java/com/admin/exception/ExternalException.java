package com.admin.exception;

import lombok.Getter;

@Getter
public class ExternalException extends RuntimeException {

    private final ErrorCode errorCode;

    public ExternalException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
