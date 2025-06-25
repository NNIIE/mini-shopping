package com.user.domain.account;

import com.relation.account.Account;
import com.relation.enums.AccountStatus;
import com.relation.enums.UserRole;

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
