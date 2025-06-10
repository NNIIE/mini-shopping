package com.user.web.response.order;

import java.math.BigDecimal;

public record OrderProductResponse(
    Long id,
    Long productId,
    BigDecimal price,
    Integer quantity
) {
}
