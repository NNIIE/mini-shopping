package com.admin.domain;

import com.storage.account.Account;
import com.storage.enums.AccountStatus;
import com.storage.enums.UserRole;

public class AccountFactory {

    /****
     * 관리자 회원가입을 위한 Account 객체를 생성합니다.
     *
     * @param email 생성할 계정의 이메일 주소
     * @param password 생성할 계정의 비밀번호
     * @return 관리자 권한과 활성 상태를 가진 Account 인스턴스
     */
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

