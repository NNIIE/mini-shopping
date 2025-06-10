package com.admin.domain;

import com.storage.account.Account;
import com.storage.enums.AccountStatus;
import com.storage.enums.UserRole;

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

