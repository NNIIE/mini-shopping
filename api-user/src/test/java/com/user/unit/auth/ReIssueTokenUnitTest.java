package com.user.unit.auth;

import com.storage.account.Account;
import com.storage.enums.DeviceType;
import com.storage.enums.TokenType;
import com.storage.token.Token;
import com.storage.user.User;
import com.user.fixture.TokenFixture;
import com.user.fixture.UserFixture;
import com.user.global.exception.BadRequestException;
import com.user.global.exception.ErrorCode;
import com.user.service.AuthService;
import com.user.service.TokenService;
import com.user.web.request.ReissueTokenRequest;
import com.user.web.response.UserTokenPairDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class ReIssueTokenUnitTest {

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthService authService;

    private User mockUser;
    private Token mockRefreshToken;
    private ReissueTokenRequest validRequest;
    private UserTokenPairDto mockTokenPair;
    private Instant testInstant;

    @BeforeEach
    void setUp() {
        testInstant = Instant.now();
        mockUser = UserFixture.createUser(UserFixture.createUserAccount());
        validRequest = TokenFixture.createRequestForReissueToken("valid.refresh.token");
        mockTokenPair = new UserTokenPairDto("new.access.token", "new.refresh.token");
        mockRefreshToken = Token.builder()
            .id(1L)
            .user(mockUser)
            .type(TokenType.REFRESH)
            .token("valid.refresh.token")
            .device(DeviceType.MAC)
            .ipAddress("123.456.789")
            .issuedAt(testInstant.minusSeconds(3600))
            .expiresAt(testInstant.plusSeconds(3600))
            .build();
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void reissueAccessToken_success() {
        // given
        when(tokenService.validateAndGetRefreshToken(anyString())).thenReturn(mockRefreshToken);
        when(tokenService.createTokenPair(isNull(), any(Instant.class))).thenReturn(mockTokenPair);

        // when
        UserTokenPairDto result = authService.reissueAccessToken(validRequest);

        // then
        assertAll(
            () -> assertThat(result).isNotNull(),
            () -> assertThat(result.accessToken()).isEqualTo("new.access.token"),
            () -> assertThat(result.refreshToken()).isEqualTo("new.refresh.token")
        );
        verify(tokenService).validateAndGetRefreshToken("valid.refresh.token");
        verify(tokenService).createTokenPair(isNull(), any(Instant.class));
        verify(tokenService).rotateRefreshToken(
            eq(mockRefreshToken),
            eq("new.refresh.token"),
            eq(DeviceType.MAC),
            eq("123.456.789"),
            any(Instant.class)
        );
    }

    @Test
    @DisplayName("토큰 재발급 실패 - 유효하지 않은 리프레시 토큰")
    void reissueAccessToken_invalid_refreshToken() {
        // given
        when(tokenService.validateAndGetRefreshToken(anyString()))
            .thenThrow(new BadRequestException(ErrorCode.INVALID_TOKEN));

        // when then
        assertThrows(BadRequestException.class, () -> authService.reissueAccessToken(validRequest));
    }

    @Test
    @DisplayName("토큰 재발급 실패 - 리프레시 토큰 만료")
    void reissueAccessToken_expired_token() {
        // given
        String expiredTokenValue = "expired.refresh.token";
        ReissueTokenRequest expiredRequest = TokenFixture.createRequestForReissueToken(expiredTokenValue);
        when(tokenService.validateAndGetRefreshToken(expiredTokenValue))
            .thenThrow(new BadRequestException(ErrorCode.INVALID_TOKEN));

        // when then
        assertThrows(BadRequestException.class, () -> authService.reissueAccessToken(expiredRequest));
    }

}
