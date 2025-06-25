package com.admin.domain;

import com.relation.account.Account;
import com.relation.enums.AccountStatus;
import com.relation.enums.UserRole;

public class AccountFactory {

    public static Account createAdminAccountForSignUp(
        final String email,
        final String password
    ) {
        return Account.builder()
            .email(email)
            .password(password)
            .role(UserRole.ADMIN)
            .status(AccountStatus.ACTIVE)
            .build();
    }

}
