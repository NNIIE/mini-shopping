package com.user.unit.auth;

import com.storage.user.User;
import com.storage.user.UserRepository;
import com.user.exception.BusinessException;
import com.user.exception.ErrorCode;
import com.user.fixture.TokenFixture;
import com.user.fixture.UserFixture;
import com.user.service.AuthService;
import com.user.service.TokenService;
import com.user.web.request.ReissueTokenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

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

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private ReissueTokenRequest validRequest;
    private String mockAccessToken;

    @BeforeEach
    void setUp() {
        User mockUser = UserFixture.createUser(UserFixture.createUserAccount());
        mockUser.setRefreshToken("valid.refresh.token");
        validRequest = TokenFixture.createRequestForReissueToken("valid.refresh.token");
        mockAccessToken = "new.access.token";
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void reissueAccessToken_success() {
        // given
        Long userId = 1L;
        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(userId);
        when(mockUser.getRefreshToken()).thenReturn("valid.refresh.token");
        when(tokenService.validateTokenAndGetUserId(anyString())).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(tokenService.createAccessToken(eq(userId), any(Instant.class))).thenReturn(mockAccessToken);

        // when
        String result = authService.reissueAccessToken(validRequest);

        // then
        assertAll(
            () -> assertThat(result).isNotNull(),
            () -> assertThat(result).isEqualTo(mockAccessToken)
        );
        verify(tokenService).validateTokenAndGetUserId("valid.refresh.token");
        verify(userRepository).findById(userId);
        verify(tokenService).createAccessToken(eq(userId), any(Instant.class));
    }

    @Test
    @DisplayName("토큰 재발급 실패 - 유효하지 않은 리프레시 토큰")
    void reissueAccessToken_invalid_refreshToken() {
        // given
        when(tokenService.validateTokenAndGetUserId(anyString()))
            .thenThrow(new BusinessException(ErrorCode.INVALID_TOKEN));

        // when then
        assertThrows(BusinessException.class, () -> authService.reissueAccessToken(validRequest));
    }

    @Test
    @DisplayName("토큰 재발급 실패 - 리프레시 토큰 만료")
    void reissueAccessToken_expired_token() {
        // given
        String expiredTokenValue = "expired.refresh.token";
        ReissueTokenRequest expiredRequest = TokenFixture.createRequestForReissueToken(expiredTokenValue);
        when(tokenService.validateTokenAndGetUserId(expiredTokenValue))
            .thenThrow(new BusinessException(ErrorCode.EXPIRED_TOKEN));

        // when then
        assertThrows(BusinessException.class, () -> authService.reissueAccessToken(expiredRequest));
    }

}
