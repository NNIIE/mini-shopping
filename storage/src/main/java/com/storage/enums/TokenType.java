package com.storage.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum TokenType {

    ACCESS, REFRESH;

    @Setter
    private long expiredMs;

}
