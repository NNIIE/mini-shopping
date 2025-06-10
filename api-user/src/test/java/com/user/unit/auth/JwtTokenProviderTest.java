package com.user.unit.auth;

import com.storage.enums.TokenType;
import com.user.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("unit")
public class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    private final Duration ACCESS_EXPIRE = Duration.ofMinutes(30);
    private final Duration REFRESH_EXPIRE = Duration.ofDays(7);

    @BeforeEach
    void setUp() {
        String SECRET_KEY = "thisisasecretkeyfortestingthejwttokenprovider12345678";
        jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, ACCESS_EXPIRE, REFRESH_EXPIRE);
        TokenType.ACCESS.setExpiredMs(ACCESS_EXPIRE.toMillis());
        TokenType.REFRESH.setExpiredMs(REFRESH_EXPIRE.toMillis());
    }

    @Test
    @DisplayName("액세스 토큰 생성 성공")
    void generateToken_forAccessToken() {
        // given
        Long userId = 1L;
        Instant issuedAt = Instant.now();

        // when
        String token = jwtTokenProvider.generateToken(TokenType.ACCESS, userId, issuedAt);
        Date expiration = jwtTokenProvider.getExpiration(token);

        // then
        assertAll(
            () -> assertThat(token).isNotNull(),
            () -> assertThat(jwtTokenProvider.isParsable(token)).isTrue(),
            () -> assertThat(jwtTokenProvider.getClaim(token, "id", Long.class)).isEqualTo(userId),
            () -> assertThat(expiration).isAfter(Date.from(issuedAt)),
            () -> assertThat(expiration).isBefore(Date.from(issuedAt.plusMillis(ACCESS_EXPIRE.toMillis() + 1000)))
        );
    }

    @Test
    @DisplayName("리프레시 토큰 생성 성공")
    void generateToken_forRefreshToken() {
        // given
        Long userId = 1L;
        Instant issuedAt = Instant.now();

        // when
        String token = jwtTokenProvider.generateToken(TokenType.REFRESH, userId, issuedAt);
        Date expiration = jwtTokenProvider.getExpiration(token);

        // then
        assertAll(
            () -> assertThat(token).isNotNull(),
            () -> assertThat(jwtTokenProvider.isParsable(token)).isTrue(),
            () -> assertThat(jwtTokenProvider.getClaim(token, "id", Long.class)).isEqualTo(userId),
            () -> assertThat(expiration).isAfter(Date.from(issuedAt)),
            () -> assertThat(expiration).isBefore(Date.from(issuedAt.plusMillis(REFRESH_EXPIRE.toMillis() + 1000)))
        );
    }

    @Test
    @DisplayName("HTTP 요청에서 Authorization 헤더 토큰 추출 성공")
    void resolveToken_WithValidAuthHeader() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String token = "validToken";
        request.addHeader("Authorization", "Bearer " + token);

        // when
        String extractedToken = jwtTokenProvider.resolveToken(request);

        // then
        assertThat(extractedToken).isEqualTo(token);
    }

    @Test
    @DisplayName("빈 Authorization 헤더로 토큰 추출 실패")
    void resolveToken_WithEmptyAuthHeader() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();

        // when
        String extractedToken = jwtTokenProvider.resolveToken(request);

        // then
        assertThat(extractedToken).isEmpty();
    }

    @Test
    @DisplayName("잘못된 형식의 Authorization 헤더로 토큰 추출 실패")
    void resolveToken_WithInvalidAuthHeader() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "InvalidPrefix validToken");

        // when
        String extractedToken = jwtTokenProvider.resolveToken(request);

        // then
        assertThat(extractedToken).isEmpty();
    }

    @Test
    @DisplayName("토큰에서 클레임 추출 성공")
    void getClaim_WithValidToken() {
        // given
        Long userId = 1L;
        Instant issuedAt = Instant.now();
        String token = jwtTokenProvider.generateToken(TokenType.ACCESS, userId, issuedAt);

        // when
        Long extractedUserId = jwtTokenProvider.getClaim(token, "id", Long.class);

        // then
        assertThat(extractedUserId).isEqualTo(userId);
    }

}

