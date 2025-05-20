package com.user.unit.token;

import com.storage.enums.DeviceType;
import com.storage.enums.TokenType;
import com.storage.token.Token;
import com.storage.token.TokenRepository;
import com.storage.user.User;
import com.user.exception.BusinessException;
import com.user.fixture.UserFixture;
import com.user.jwt.JwtTokenProvider;
import com.user.service.TokenService;
import com.user.web.response.UserTokenDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class TokenUnitTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenService tokenService;

    private User mockUser;
    private Token mockToken;

    @BeforeEach
    void setUp() {
        mockUser = UserFixture.createUser(UserFixture.createUserAccount());
        Instant now = Instant.now();
        mockToken = Token.builder()
            .id(1L)
            .user(mockUser)
            .type(TokenType.REFRESH)
            .token("validRefreshToken")
            .device(DeviceType.MAC)
            .ipAddress("127.0.0.1")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(3600))
            .build();
    }

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
    @DisplayName("유효한 리프레시 토큰 검증 및 조회 성공")
    void validate_and_get_refreshToken() {
        // given
        String validRefreshToken = "validRefreshToken";
        given(jwtTokenProvider.isParsable(validRefreshToken)).willReturn(true);
        given(jwtTokenProvider.getClaim(validRefreshToken, "id", Long.class)).willReturn(1L);
        given(tokenRepository.findByUserIdAndTokenAndType(anyLong(), anyString(), any(TokenType.class)))
            .willReturn(Optional.of(mockToken));

        // when
        Token result = tokenService.validateAndGetRefreshToken(validRefreshToken);

        // then
        assertAll(
            () -> assertThat(result).isNotNull(),
            () -> assertThat(result.getToken()).isEqualTo("validRefreshToken")
        );
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
    @DisplayName("refresh 토큰 발급 성공")
    void issue_refreshToken() {
        // given
        String refreshToken = "newRefreshToken";
        DeviceType device = DeviceType.MAC;
        String ipAddress = "127.0.0.1";
        Instant issuedAt = Instant.now();

        // when
        tokenService.issueRefreshToken(mockUser, refreshToken, device, ipAddress, issuedAt);

        // then
        verify(tokenRepository).deleteByUserIdAndTypeAndDevice(mockUser.getId(), TokenType.REFRESH, device);
        verify(tokenRepository).save(any(Token.class));
    }

}
