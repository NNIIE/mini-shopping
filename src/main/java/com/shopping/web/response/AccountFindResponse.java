package com.shopping.web.response;

import com.shopping.global.enums.AccountStatus;
import com.shopping.global.enums.UserRole;

public record AccountFindResponse(
    Long id,
    String email,
    UserRole role,
    AccountStatus status
) {
}
