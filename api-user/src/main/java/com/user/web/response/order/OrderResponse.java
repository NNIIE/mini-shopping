package com.user.web.response.order;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
    Long orderId,
    List<OrderProductResponse> orderProducts,
    BigDecimal totalPrice
) {
}
