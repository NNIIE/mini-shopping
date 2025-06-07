package com.admin.web.response.product;

import java.math.BigDecimal;

public record ProductResponse(
    Long id,
    Long brandId,
    Long categoryId,
    String name,
    BigDecimal price,
    Integer stock
) {
}
