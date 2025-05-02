package com.shopping.unit.service;

import com.shopping.fixture.UserFixture;
import com.shopping.global.exception.ConflictException;
import com.shopping.global.exception.ErrorCode;
import com.shopping.service.AccountService;
import com.shopping.service.UserService;
import com.shopping.storage.account.Account;
import com.shopping.storage.user.User;
import com.shopping.storage.user.UserRepository;
import com.shopping.web.request.UserSignUpRequest;
import com.shopping.web.response.UserSignUpResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension .class)
class UserServiceSignUpUnitTest {

    @InjectMocks
    private UserService UserService;

    @Mock
    private AccountService accountService;

    @Mock
    private UserRepository userRepository;


    @Test
    @DisplayName("회원 가입 - 성공")
    void userSignUpSuccess() {
        // Given
        UserSignUpRequest request = UserFixture.createRequestForUserSignUp();
        Account account = UserFixture.createUserAccount();
        User user = UserFixture.createUser(account);
        when(userRepository.findByNickname(request.getNickname())).thenReturn(Optional.empty());
        when(accountService.createUserAccount(request.getEmail(), request.getPassword())).thenReturn(account);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        UserSignUpResponse response = UserService.userSignUp(request);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        // Then
        assertAll(
            () -> assertThat(response).isNotNull(),
            () -> assertThat(response.email()).isEqualTo(request.getEmail()),
            () -> assertThat(response.nickname()).isEqualTo(request.getNickname()),
            () -> assertThat(savedUser.getNickname()).isEqualTo(request.getNickname()),
            () -> assertThat(savedUser.getPhoneNumber()).isEqualTo(request.getPhoneNumber()),
            () -> assertThat(savedUser.getAccount()).isEqualTo(account)
        );
    }

    @Test
    @DisplayName("회원 가입 - 중복 이메일")
    void userSignUpExistsEmail() {
        // Given
        UserSignUpRequest request = UserFixture.createRequestForUserSignUp();
        when(accountService.createUserAccount(request.getEmail(), request.getPassword()))
            .thenThrow(new ConflictException(ErrorCode.EMAIL_CONFLICT));

        // When & Then
        assertThatThrownBy(() -> UserService.userSignUp(request))
            .isInstanceOf(ConflictException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.EMAIL_CONFLICT);
    }

    @Test
    @DisplayName("회원 가입 - 중복 닉네임")
    void userSignUpExistsNickname() {
        // Given
        UserSignUpRequest request = UserFixture.createRequestForUserSignUp();
        when(accountService.createUserAccount(request.getEmail(), request.getPassword()))
            .thenThrow(new ConflictException(ErrorCode.NICKNAME_CONFLICT));

        // When & Then
        assertThatThrownBy(() -> UserService.userSignUp(request))
            .isInstanceOf(ConflictException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.NICKNAME_CONFLICT);
    }

}
