package com.user.domain.account;

import com.storage.account.Account;
import com.storage.enums.AccountStatus;
import com.storage.enums.UserRole;

public class AccountFactory {

    /**
     * 회원 가입을 위한 기본 사용자 계정을 생성합니다.
     *
     * @param email 생성할 계정의 이메일 주소
     * @param password 생성할 계정의 비밀번호
     * @return 활성 상태이며 BASIC 역할이 할당된 Account 객체
     */
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

