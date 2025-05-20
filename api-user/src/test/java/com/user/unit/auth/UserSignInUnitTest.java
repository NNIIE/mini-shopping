package com.user.unit.auth;

import com.storage.enums.DeviceType;
import com.storage.user.User;
import com.storage.user.UserRepository;
import com.user.exception.BusinessException;
import com.user.fixture.UserFixture;
import com.user.service.AuthService;
import com.user.service.PasswordEncoder;
import com.user.service.TokenService;
import com.user.web.request.UserSignInRequest;
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
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class UserSignInUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthService authService;

    private User mockUser;
    private UserSignInRequest userSignInRequest;

    @BeforeEach
    void setUp() {
        mockUser = UserFixture.createUser(UserFixture.createUserAccount());
        userSignInRequest = UserFixture.createRequestForUserSignIn();
    }

    @Test
    @DisplayName("로그인 성공")
    void signIn_success() {
        // Given
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(mockUser));
        given(passwordEncoder.verifyPassword(anyString(), anyString())).willReturn(true);
        given(tokenService.createAccessAndRefreshToken(any(), any())).willReturn(new UserTokenDto("accessToken", "refreshToken"));

        // When
        UserTokenDto result = authService.signIn(userSignInRequest);

        // Then
        assertAll(
            () -> assertThat(result).isNotNull(),
            () -> assertThat(Objects.requireNonNull(result).accessToken()).isEqualTo("accessToken"),
            () -> assertThat(Objects.requireNonNull(result).refreshToken()).isEqualTo("refreshToken")
        );

        verify(tokenService).issueRefreshToken(
            any(User.class),
            anyString(),
            any(DeviceType.class),
            anyString(),
            any(Instant.class));
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 실패")
    void signIn_not_found_email() {
        // Given
        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        // When Then
        assertThrows(BusinessException.class, () -> authService.signIn(userSignInRequest));
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 실패")
    void signIn_invalid_password() {
        // Given
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(mockUser));
        given(passwordEncoder.verifyPassword(anyString(), anyString())).willReturn(false);

        // When Then
        assertThrows(BusinessException.class, () -> authService.signIn(userSignInRequest));
    }

}
