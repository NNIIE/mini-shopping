package com.relation.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum TokenType {

    ACCESS, REFRESH;

    @Setter
    private long expiredMs;

}
