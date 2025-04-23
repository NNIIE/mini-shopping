package com.shopping.fixture;

import com.shopping.model.UserRole;
import com.shopping.model.request.UserSignUpRequest;
import org.springframework.test.util.ReflectionTestUtils;

public class UserFixture {

    public static UserSignUpRequest createSignUpRequest() {
        UserSignUpRequest request = new UserSignUpRequest();
        ReflectionTestUtils.setField(request, "email", "test@email.com");
        ReflectionTestUtils.setField(request, "nickname", "testUser");
        ReflectionTestUtils.setField(request, "password", "Qwer1234!!");
        ReflectionTestUtils.setField(request, "role", UserRole.BASIC);

        return request;
    }

    public static UserSignUpRequest createSignUpRequestByParameter(
            final String email,
            final String nickname,
            final String password,
            final UserRole role
    ) {
        UserSignUpRequest request = new UserSignUpRequest();
        ReflectionTestUtils.setField(request, "email", email);
        ReflectionTestUtils.setField(request, "nickname", nickname);
        ReflectionTestUtils.setField(request, "password", password);
        ReflectionTestUtils.setField(request, "role", role);

        return request;
    }

}
