package com.user.domain.user;

import com.relation.account.Account;
import com.relation.user.User;

public class UserFactory {

    public static User createUserForSignUp(
        final Account account,
        final String nickname,
        final String phoneNumber
    ) {
        return User.builder()
            .account(account)
            .nickname(nickname)
            .phoneNumber(phoneNumber)
            .build();
    }

}
