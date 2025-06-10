package com.user.fixture;

import com.user.web.request.auth.ReissueTokenRequest;
import org.springframework.test.util.ReflectionTestUtils;

public class TokenFixture {

    /**
     * 주어진 값을 refreshToken 필드에 설정한 ReissueTokenRequest 객체를 생성하여 반환합니다.
     *
     * @param value refreshToken으로 설정할 값
     * @return refreshToken 필드가 지정된 값으로 설정된 ReissueTokenRequest 객체
     */
    public static ReissueTokenRequest createRequestForReissueToken(String value) {
        ReissueTokenRequest request = new ReissueTokenRequest();
        ReflectionTestUtils.setField(request, "refreshToken", value);

        return request;
    }

}

