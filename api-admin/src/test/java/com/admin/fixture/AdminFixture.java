package com.admin.fixture;

import com.admin.web.request.auth.AdminSignInRequest;
import com.admin.web.request.auth.AdminSignUpRequest;
import com.storage.account.Account;
import com.storage.admin.Admin;
import com.storage.enums.AccountStatus;
import com.storage.enums.UserRole;
import org.springframework.test.util.ReflectionTestUtils;

public class AdminFixture {

    private static final String PASSWORD = "Qwer1234!!";
    private static final String PHONE_NUMBER = "010-1234-5678";

    public static Account createAdminAccount() {
        return Account.builder()
            .email("admintest@email.com")
            .password(PASSWORD)
            .role(UserRole.ADMIN)
            .status(AccountStatus.ACTIVE)
            .build();
    }

    public static Admin createAdminWithAnId() {
        Admin admin = new Admin(createAdminAccount());
        ReflectionTestUtils.setField(admin, "id", 1L);

        return admin;
    }

    public static AdminSignUpRequest createRequestForAdminSignUp() {
        AdminSignUpRequest request = new AdminSignUpRequest();
        ReflectionTestUtils.setField(request, "email", "admintest@email.com");
        ReflectionTestUtils.setField(request, "password", PASSWORD);

        return request;
    }

    public static AdminSignUpRequest createRequestForAdminSignUpParameter(final String email, final String password) {
        AdminSignUpRequest request = new AdminSignUpRequest();
        ReflectionTestUtils.setField(request, "email", email);
        ReflectionTestUtils.setField(request, "password", password);

        return request;
    }

    /**
     * 지정된 이메일과 비밀번호로 AdminSignInRequest 테스트 객체를 생성합니다.
     *
     * @param email 관리자 계정의 이메일
     * @param password 관리자 계정의 비밀번호
     * @return 주어진 이메일과 비밀번호가 설정된 AdminSignInRequest 객체
     */
    public static AdminSignInRequest createRequestForAdminSignIn(final String email, final String password) {
        AdminSignInRequest request = new AdminSignInRequest();
        ReflectionTestUtils.setField(request, "email", email);
        ReflectionTestUtils.setField(request, "password", password);

        return request;
    }

}

