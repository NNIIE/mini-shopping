package com.shopping.fixture;

import com.shopping.service.domain.AccountStatus;
import com.shopping.service.domain.UserRole;
import com.shopping.storage.entity.Account;
import com.shopping.storage.entity.User;
import com.shopping.web.request.AdminSignUpRequest;
import com.shopping.web.request.UserSignUpRequest;
import org.springframework.test.util.ReflectionTestUtils;

public class UserFixture {

    private static final String PASSWORD = "Qwer1234!!";
    private static final String PHONE_NUMBER = "010-1234-5678";

    public static User createUser(final Account account) {
        return User.builder()
            .account(account)
            .nickname("testUser")
            .phoneNumber(PHONE_NUMBER)
            .build();
    }

    public static Account createUserAccount() {
        return Account.builder()
            .email("usertest@email.com")
            .password(PASSWORD)
            .role(UserRole.BASIC)
            .status(AccountStatus.ACTIVE)
            .build();
    }

    public static Account createAdminAccount() {
        return Account.builder()
            .email("admintest@email.com")
            .password(PASSWORD)
            .role(UserRole.ADMIN)
            .status(AccountStatus.ACTIVE)
            .build();
    }

    public static UserSignUpRequest createRequestForUserSignUp() {
        UserSignUpRequest request = new UserSignUpRequest();
        ReflectionTestUtils.setField(request, "email", "usertest@email.com");
        ReflectionTestUtils.setField(request, "nickname", "testUser");
        ReflectionTestUtils.setField(request, "password", PASSWORD);
        ReflectionTestUtils.setField(request, "phoneNumber", PHONE_NUMBER);

        return request;
    }

    public static UserSignUpRequest createRequestForUserSignUpParameter(
            final String email,
            final String nickname,
            final String password,
            final String phoneNumber
    ) {
        UserSignUpRequest request = new UserSignUpRequest();
        ReflectionTestUtils.setField(request, "email", email);
        ReflectionTestUtils.setField(request, "nickname", nickname);
        ReflectionTestUtils.setField(request, "password", password);
        ReflectionTestUtils.setField(request, "phoneNumber", phoneNumber);

        return request;
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
