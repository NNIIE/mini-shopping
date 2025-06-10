package com.user.fixture;

import com.user.web.request.auth.ReissueTokenRequest;
import org.springframework.test.util.ReflectionTestUtils;

public class TokenFixture {

    public static ReissueTokenRequest createRequestForReissueToken(String value) {
        ReissueTokenRequest request = new ReissueTokenRequest();
        ReflectionTestUtils.setField(request, "refreshToken", value);

        return request;
    }

}

