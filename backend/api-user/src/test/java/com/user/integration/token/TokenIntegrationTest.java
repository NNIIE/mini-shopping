package com.user.integration.token;

import com.relation.account.Account;
import com.relation.account.AccountRepository;
import com.relation.enums.AccountStatus;
import com.relation.enums.TokenType;
import com.relation.enums.UserRole;
import com.relation.user.User;
import com.relation.user.UserRepository;
import com.user.exception.BusinessException;
import com.user.fixture.TokenFixture;
import com.user.jwt.JwtTokenProvider;
import com.user.service.AuthService;
import com.user.service.TokenService;
import com.user.web.request.auth.ReissueTokenRequest;
import com.user.web.response.auth.UserTokenDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
@SpringBootTest
@DirtiesContext
@Transactional
class TokenIntegrationTest {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private String validAccessToken;
    private String validRefreshToken;

    @BeforeEach
    void setUp() {
        Account account = Account.builder()
            .email("token-test@example.com")
            .password(passwordEncoder.encode("Qwer1234!!"))
            .role(UserRole.BASIC)
            .status(AccountStatus.ACTIVE)
            .build();
        accountRepository.save(account);

        testUser = User.builder()
            .account(account)
            .nickname("tokenTestUser")
            .phoneNumber("01012345678")
            .build();
        userRepository.save(testUser);

        Instant issuedAt = Instant.now();
        validAccessToken = jwtTokenProvider.generateToken(TokenType.ACCESS, testUser.getId(), issuedAt);
        validRefreshToken = jwtTokenProvider.generateToken(TokenType.REFRESH, testUser.getId(), issuedAt);

        testUser.setRefreshToken(validRefreshToken);
        userRepository.save(testUser);
    }

    @Test
    @DisplayName("토큰에서 사용자 ID 추출 성공")
    void validateAccessTokenAndGetUserId_WithValidToken_ShouldReturnUserId() {
        // when
        Long userId = tokenService.validateAccessTokenAndGetUserId(validAccessToken);

        // then
        assertThat(userId).isEqualTo(testUser.getId());
    }

    @Test
    @DisplayName("예외 발생 - 잘못된 엑세스 토큰")
    void validateAccessTokenAndGetUserId_WithInvalidFormat_ShouldThrowException() {
        // given
        String invalidToken = "this.is.invalid.access.token";

        // when & then
        assertThrows(BusinessException.class, () -> tokenService.validateAccessTokenAndGetUserId(invalidToken));
    }

    @Test
    @DisplayName("예외 발생 - 만료된 액세스 토큰")
    void validateAccessTokenAndGetUserId_WithExpiredToken() {
        // given
        Instant expiredIssuedAt = Instant.now().minus(2, ChronoUnit.HOURS);
        String expiredToken = jwtTokenProvider.generateToken(TokenType.ACCESS, testUser.getId(), expiredIssuedAt);

        // when & then
        assertThrows(BusinessException.class, () -> tokenService.validateAccessTokenAndGetUserId(expiredToken));
    }

    @Test
    @DisplayName("리프레시 토큰에서 사용자 ID 추출 성공")
    void validateTokenAndGetUserId_WithValidToken_ShouldReturnUserId() {
        // when
        Long userId = tokenService.validateTokenAndGetUserId(validRefreshToken);

        // then
        assertThat(userId).isEqualTo(testUser.getId());
    }

    @Test
    @DisplayName("예외 발생 - 잘못된 리프레시 토큰")
    void validateTokenAndGetUserId_WithInvalidFormat_ShouldThrowException() {
        // given
        String invalidToken = "this.is.invalid.refresh.token";

        // when & then
        assertThrows(BusinessException.class, () -> tokenService.validateTokenAndGetUserId(invalidToken));
    }

    @Test
    @DisplayName("예외 발생 - 만료된 리프레시 토큰")
    void validateTokenAndGetUserId_WithExpiredToken() {
        // given
        long refreshTokenExpiryMs = TokenType.REFRESH.getExpiredMs();
        Instant expiredIssuedAt = Instant.now().minus(refreshTokenExpiryMs + 86400000, ChronoUnit.MILLIS);
        String expiredToken = jwtTokenProvider.generateToken(TokenType.REFRESH, testUser.getId(), expiredIssuedAt);

        // when & then
        assertThrows(BusinessException.class, () -> tokenService.validateTokenAndGetUserId(expiredToken));
    }

    @Test
    @DisplayName("HTTP 요청에서 토큰 추출 성공")
    void extractAndValidateToken_WithValidHeader() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + validAccessToken);

        // when
        String extractedToken = tokenService.extractAndValidateToken(request);

        // then
        assertThat(extractedToken).isEqualTo(validAccessToken);
    }

    @Test
    @DisplayName("access-refresh 토큰 생성 성공")
    void createTokenPair() {
        // when
        UserTokenDto tokenPair = tokenService.createAccessAndRefreshToken(testUser.getId(), Instant.now());
        Long userIdFromAccess = jwtTokenProvider.getClaim(tokenPair.accessToken(), "id", Long.class);
        Long userIdFromRefresh = jwtTokenProvider.getClaim(tokenPair.refreshToken(), "id", Long.class);

        // then
        assertAll(
            () -> assertThat(tokenPair).isNotNull(),
            () -> assertThat(tokenPair.accessToken()).isNotBlank(),
            () -> assertThat(tokenPair.refreshToken()).isNotBlank(),
            () -> assertThat(userIdFromAccess).isEqualTo(testUser.getId()),
            () -> assertThat(userIdFromRefresh).isEqualTo(testUser.getId())
        );
    }

    @Test
    @DisplayName("액세스 토큰만 생성 성공")
    void createAccessToken() {
        // when
        String accessToken = tokenService.createAccessToken(testUser.getId(), Instant.now());
        Long userIdFromAccess = jwtTokenProvider.getClaim(accessToken, "id", Long.class);

        // then
        assertAll(
            () -> assertThat(accessToken).isNotBlank(),
            () -> assertThat(userIdFromAccess).isEqualTo(testUser.getId())
        );
    }

    @Test
    @DisplayName("리프레시 토큰으로 액세스 토큰 재발급 성공")
    void reissueAccessToken_success() {
        // given
        ReissueTokenRequest request = TokenFixture.createRequestForReissueToken(validRefreshToken);

        // when
        String newAccessToken = authService.reissueAccessToken(request);
        Long userIdFromToken = jwtTokenProvider.getClaim(newAccessToken, "id", Long.class);

        // then
        assertAll(
            () -> assertThat(newAccessToken).isNotNull(),
            () -> assertThat(newAccessToken).isNotBlank(),
            () -> assertThat(userIdFromToken).isEqualTo(testUser.getId())
        );
    }

    @Test
    @DisplayName("리프레시 토큰으로 액세스 토큰 재발급 실패 - 사용자와 토큰 불일치")
    void reissueAccessToken_failure_tokenMismatch() {
        // given
        String differentRefreshToken = "eyJhbGciOiJIUzM4NCJ9.different_token_value.signature";
        ReissueTokenRequest request = TokenFixture.createRequestForReissueToken(differentRefreshToken);

        // then
        assertThrows(BusinessException.class, () -> authService.reissueAccessToken(request));
    }

    @Test
    @DisplayName("리프레시 토큰으로 액세스 토큰 재발급 실패 - 만료된 토큰")
    void reissueAccessToken_failure_expiredToken() {
        // given
        Instant expiredIssuedAt = Instant.now().minus(8, ChronoUnit.DAYS);
        String expiredRefreshToken = jwtTokenProvider.generateToken(TokenType.REFRESH, testUser.getId(), expiredIssuedAt);
        ReissueTokenRequest request = TokenFixture.createRequestForReissueToken(expiredRefreshToken);

        // when & then
        assertThrows(BusinessException.class, () -> authService.reissueAccessToken(request));
    }

}
