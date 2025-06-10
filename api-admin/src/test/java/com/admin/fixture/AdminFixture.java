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

    public static AdminSignInRequest createRequestForAdminSignIn(final String email, final String password) {
        AdminSignInRequest request = new AdminSignInRequest();
        ReflectionTestUtils.setField(request, "email", email);
        ReflectionTestUtils.setField(request, "password", password);

        return request;
    }

}

