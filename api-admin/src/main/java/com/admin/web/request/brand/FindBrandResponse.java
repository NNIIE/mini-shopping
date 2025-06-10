package com.admin.web.request.brand;

import java.time.Instant;

public record FindBrandResponse(
    Long id,
    Long adminId,
    String name,
    String businessNumber,
    Instant createdAt
) {
}
