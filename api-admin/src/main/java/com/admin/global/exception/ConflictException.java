package com.admin.global.exception;

import lombok.Getter;

@Getter
public class ConflictException extends CustomException {

    public ConflictException(final ErrorCode errorCode) {
        super(errorCode);
    }

}
