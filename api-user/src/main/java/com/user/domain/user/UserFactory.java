package com.user.domain.user;

import com.storage.account.Account;
import com.storage.user.User;

public class UserFactory {

    /**
     * 회원 가입을 위한 User 객체를 생성합니다.
     *
     * @param account 회원의 계정 정보
     * @param nickname 회원의 닉네임
     * @param phoneNumber 회원의 전화번호
     * @return 생성된 User 객체
     */
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

