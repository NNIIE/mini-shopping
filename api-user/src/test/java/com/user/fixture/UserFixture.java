package com.user.fixture;

import com.storage.account.Account;
import com.storage.enums.AccountStatus;
import com.storage.enums.UserRole;
import com.storage.user.User;
import com.user.web.request.auth.UserSignInRequest;
import com.user.web.request.auth.UserSignUpRequest;
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

    public static UserSignInRequest createRequestForUserSignIn() {
        UserSignInRequest request = new UserSignInRequest();
        ReflectionTestUtils.setField(request, "email", "usertest@email.com");
        ReflectionTestUtils.setField(request, "password", PASSWORD);

        return request;
    }

    public static UserSignInRequest createRequestForUserSignInParameter(
        final String email,
        final String password
    ) {
        UserSignInRequest request = new UserSignInRequest();
        ReflectionTestUtils.setField(request, "email", email);
        ReflectionTestUtils.setField(request, "password", password);

        return request;
    }

}
