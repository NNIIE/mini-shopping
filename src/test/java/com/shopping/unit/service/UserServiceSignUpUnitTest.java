package com.shopping.unit.service;

import com.shopping.exception.ApiException;
import com.shopping.exception.ErrorCode;
import com.shopping.fixture.UserFixture;
import com.shopping.model.UserRole;
import com.shopping.model.UserStatus;
import com.shopping.model.entity.User;
import com.shopping.model.request.UserSignUpRequest;
import com.shopping.repository.UserRepository;
import com.shopping.service.PasswordEncoder;
import com.shopping.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension .class)
class UserServiceSignUpUnitTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("회원 가입 - 성공")
    void signUpSuccess() {
        // Given
        UserSignUpRequest signUpRequest = UserFixture.createSignUpRequest();
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByNickname(signUpRequest.getNickname())).thenReturn(false);

        // When
        userService.signUp(signUpRequest);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        // Then
        assertAll(
            () -> assertThat(savedUser.getEmail()).isEqualTo(signUpRequest.getEmail()),
            () -> assertThat(savedUser.getNickname()).isEqualTo(signUpRequest.getNickname()),
            () -> assertThat(savedUser.getPassword()).isEqualTo(passwordEncoder.encode(signUpRequest.getPassword())),
            () -> assertThat(savedUser.getRole()).isEqualTo(UserRole.BASIC),
            () -> assertThat(savedUser.getStatus()).isEqualTo(UserStatus.ACTIVE)
        );

    }

    @Test
    @DisplayName("회원 가입 - 중복 이메일")
    void signUpExistsEmail() {
        // Given
        UserSignUpRequest signUpRequest = UserFixture.createSignUpRequest();
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.signUp(signUpRequest))
            .isInstanceOf(ApiException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.EMAIL_CONFLICT);

    }

    @Test
    @DisplayName("회원 가입 - 중복 닉네임")
    void signUpExistsNickname() {
        // Given
        UserSignUpRequest signUpRequest = UserFixture.createSignUpRequest();
        when(userRepository.existsByNickname(signUpRequest.getNickname())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.signUp(signUpRequest))
            .isInstanceOf(ApiException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.NICKNAME_CONFLICT);

    }

}
