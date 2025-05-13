package com.user.global.exception;

public class NotFoundException extends CustomException {

    public NotFoundException(final ErrorCode errorCode) {
        super(errorCode);
    }

}
