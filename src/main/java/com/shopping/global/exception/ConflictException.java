package com.shopping.global.exception;

import lombok.Getter;

@Getter
public class ConflictException extends CustomException {

    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }

}
