package com.user.domain.account;

import com.storage.account.Account;
import com.storage.enums.AccountStatus;
import com.storage.enums.UserRole;

public class AccountFactory {

    public static Account createUserAccountForSignUp(
        final String email,
        final String password
    ) {
        return Account.builder()
            .email(email)
            .password(password)
            .role(UserRole.BASIC)
            .status(AccountStatus.ACTIVE)
            .build();
    }

}

