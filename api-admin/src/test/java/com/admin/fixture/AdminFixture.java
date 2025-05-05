package com.admin.fixture;

import com.admin.web.request.AdminSignUpRequest;
import com.storage.account.Account;
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

}
