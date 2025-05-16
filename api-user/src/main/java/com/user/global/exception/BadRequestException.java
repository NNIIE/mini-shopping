package com.user.global.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends CustomException {

    public BadRequestException(final ErrorCode errorCode) {
        super(errorCode);
    }

}
