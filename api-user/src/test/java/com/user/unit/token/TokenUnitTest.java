package com.user.unit.token;

import com.storage.enums.TokenType;
import com.user.exception.BusinessException;
import com.user.jwt.JwtTokenProvider;
import com.user.service.TokenService;
import com.user.web.response.auth.UserTokenDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class TokenUnitTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private TokenService tokenService;

    @Test
    @DisplayName("액세스 토큰 검증 및 사용자 ID 추출 성공")
    void validate_accessToken_and_getUserId() {
        // given
        String validToken = "validAccessToken";
        given(jwtTokenProvider.isParsable(validToken)).willReturn(true);
        given(jwtTokenProvider.getExpiration(validToken)).willReturn(new Date(System.currentTimeMillis() + 3600000));
        given(jwtTokenProvider.getClaim(validToken, "id", Long.class)).willReturn(1L);

        // when
        Long userId = tokenService.validateAccessTokenAndGetUserId(validToken);

        // then
        assertThat(userId).isEqualTo(1L);
    }

    @Test
    @DisplayName("형식이 잘못된 토큰 검증 실패")
    void validate_jwtFormat() {
        // given
        String invalidToken = "invalidToken";
        given(jwtTokenProvider.isParsable(invalidToken)).willReturn(false);

        // when, then
        assertThrows(BusinessException.class, () -> tokenService.validateJwtFormat(invalidToken));
    }

    @Test
    @DisplayName("만료된 토큰 검증 실패")
    void validate_jwtExpiration() {
        // given
        String expiredToken = "expiredToken";
        given(jwtTokenProvider.getExpiration(expiredToken)).willReturn(new Date(System.currentTimeMillis() - 3600000));

        // when, then
        assertThrows(BusinessException.class, () -> tokenService.validateJwtExpiration(expiredToken));
    }

    @Test
    @DisplayName("리프레시 토큰 검증 및 사용자 ID 추출 성공")
    void validateTokenAndGetUserId_success() {
        // given
        String validRefreshToken = "validRefreshToken";
        given(jwtTokenProvider.isParsable(validRefreshToken)).willReturn(true);
        given(jwtTokenProvider.getExpiration(validRefreshToken)).willReturn(new Date(System.currentTimeMillis() + 3600000));
        given(jwtTokenProvider.getClaim(validRefreshToken, "id", Long.class)).willReturn(1L);

        // when
        Long userId = tokenService.validateTokenAndGetUserId(validRefreshToken);

        // then
        assertThat(userId).isEqualTo(1L);
    }

    @Test
    @DisplayName("ID가 없는 리프레시 토큰 검증 실패")
    void validateTokenAndGetUserId_null_id() {
        // given
        String validFormatToken = "validFormatToken";
        given(jwtTokenProvider.isParsable(validFormatToken)).willReturn(true);
        given(jwtTokenProvider.getExpiration(validFormatToken)).willReturn(new Date(System.currentTimeMillis() + 3600000));
        given(jwtTokenProvider.getClaim(validFormatToken, "id", Long.class)).willReturn(null);

        // when, then
        assertThrows(BusinessException.class, () -> tokenService.validateTokenAndGetUserId(validFormatToken));
    }

    @Test
    @DisplayName("access-refresh 토큰 생성 성공")
    void create_tokenPair() {
        // given
        Long userId = 1L;
        Instant issuedAt = Instant.now();
        given(jwtTokenProvider.generateToken(TokenType.ACCESS, userId, issuedAt)).willReturn("accessToken");
        given(jwtTokenProvider.generateToken(TokenType.REFRESH, userId, issuedAt)).willReturn("refreshToken");

        // when
        UserTokenDto result = tokenService.createAccessAndRefreshToken(userId, issuedAt);

        // then
        assertAll(
            () -> assertThat(result).isNotNull(),
            () -> assertThat(result.accessToken()).isEqualTo("accessToken"),
            () -> assertThat(result.refreshToken()).isEqualTo("refreshToken")
        );
    }

    @Test
    @DisplayName("액세스 토큰만 생성 성공")
    void create_accessToken() {
        // given
        Long userId = 1L;
        Instant issuedAt = Instant.now();
        given(jwtTokenProvider.generateToken(TokenType.ACCESS, userId, issuedAt)).willReturn("accessToken");

        // when
        String result = tokenService.createAccessToken(userId, issuedAt);

        // then
        assertThat(result).isEqualTo("accessToken");
    }

    @Test
    @DisplayName("HTTP 요청에서 토큰 추출 성공")
    void extractAndValidateToken() {
        // given
        jakarta.servlet.http.HttpServletRequest request = new org.springframework.mock.web.MockHttpServletRequest();
        given(jwtTokenProvider.resolveToken(any())).willReturn("extractedToken");

        // when
        String result = tokenService.extractAndValidateToken(request);

        // then
        assertThat(result).isEqualTo("extractedToken");
    }

}
