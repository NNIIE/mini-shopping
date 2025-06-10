package com.admin.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    /**
     * 지정된 에러 코드를 사용하여 비즈니스 예외를 생성합니다.
     *
     * @param errorCode 발생한 비즈니스 예외의 상세 정보를 담고 있는 에러 코드
     */
    public BusinessException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}

