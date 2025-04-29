package com.shopping.service.domain;

public enum OrderStatus {

    BEFORE_PAYMENT,     // 결제 전
    AFTER_PAYMENT,      // 결제 후
    SHIPPING,           // 배송 중
    COMPLETED,          // 배송 완료
    CANCELED,           // 취소

}
