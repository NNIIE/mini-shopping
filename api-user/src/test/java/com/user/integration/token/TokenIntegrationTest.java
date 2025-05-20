package com.user.integration.token;

import com.storage.account.Account;
import com.storage.account.AccountRepository;
import com.storage.enums.AccountStatus;
import com.storage.enums.DeviceType;
import com.storage.enums.TokenType;
import com.storage.enums.UserRole;
import com.storage.token.Token;
import com.storage.token.TokenRepository;
import com.storage.user.User;
import com.storage.user.UserRepository;
import com.user.exception.BusinessException;
import com.user.jwt.JwtTokenProvider;
import com.user.service.TokenService;
import com.user.web.response.UserTokenDto;
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
import java.util.List;
import java.util.Optional;

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
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private String validAccessToken;
    private String validRefreshToken;
    private Instant issuedAt;

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

        issuedAt = Instant.now();
        validAccessToken = jwtTokenProvider.generateToken(TokenType.ACCESS, testUser.getId(), issuedAt);
        validRefreshToken = jwtTokenProvider.generateToken(TokenType.REFRESH, testUser.getId(), issuedAt);
        tokenService.issueRefreshToken(
            testUser,
            validRefreshToken,
            DeviceType.MAC,
            "127.0.0.1",
            issuedAt
        );
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
    @DisplayName("유효한 리프레시 토큰으로 토큰 조회 성공")
    void validateAndGetRefreshToken_WithValidToken() {
        // when
        Token token = tokenService.validateAndGetRefreshToken(validRefreshToken);

        // then
        assertAll(
            () -> assertThat(token).isNotNull(),
            () -> assertThat(token.getToken()).isEqualTo(validRefreshToken),
            () -> assertThat(token.getUser().getId()).isEqualTo(testUser.getId())
        );
    }

    @Test
    @DisplayName("존재하지 않는 리프레시 토큰으로 예외 발생")
    void validateAndGetRefreshToken_WithNonExistentToken() {
        // given
        String nonExistentToken = jwtTokenProvider.generateToken(
            TokenType.REFRESH,
            Long.MAX_VALUE,
            Instant.now()
        );

        // when & then
        BusinessException exception = assertThrows(BusinessException.class, () -> tokenService.validateAndGetRefreshToken(nonExistentToken));
        assertThat(exception.getErrorCode().name()).isEqualTo("INVALID_TOKEN");
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
        );;
    }

    @Test
    @DisplayName("동일 디바이스에 대한 리프레시 토큰 발급 시 기존 토큰 삭제")
    void issueRefreshToken_WithExistingTokenForSameDevice() {
        // given
        DeviceType device = DeviceType.MAC;
        String newRefreshToken = jwtTokenProvider.generateToken(TokenType.REFRESH, testUser.getId(), Instant.now());
        String ipAddress = "192.168.1.2";
        Instant now = Instant.now();
        List<Token> tokensBeforeIssue = tokenRepository.findAll();
        assertThat(tokensBeforeIssue).hasSize(1);

        // when
        tokenService.issueRefreshToken(testUser, newRefreshToken, device, ipAddress, now);

        // then
        List<Token> tokensAfterIssue = tokenRepository.findAll();
        assertThat(tokensAfterIssue).hasSize(1);

        Optional<Token> savedToken = tokenRepository.findByUserIdAndTokenAndType(
            testUser.getId(), newRefreshToken, TokenType.REFRESH);
        assertThat(savedToken).isPresent();
        assertThat(savedToken.get().getToken()).isEqualTo(newRefreshToken);
    }

}
