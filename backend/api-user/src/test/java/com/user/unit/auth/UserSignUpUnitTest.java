package com.user.unit.auth;

import com.relation.account.Account;
import com.relation.account.AccountRepository;
import com.relation.enums.AccountStatus;
import com.relation.enums.UserRole;
import com.relation.user.User;
import com.relation.user.UserRepository;
import com.user.exception.BusinessException;
import com.user.fixture.UserFixture;
import com.user.service.AuthService;
import com.user.service.PasswordEncoder;
import com.user.web.request.auth.UserSignUpRequest;
import com.user.web.response.auth.UserSignUpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class UserSignUpUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User mockUser;
    private Account mockAccount;
    private UserSignUpRequest validSignUpRequest;

    @BeforeEach
    void setUp() {
        mockAccount = UserFixture.createUserAccount();
        mockUser = UserFixture.createUser(mockAccount);
        validSignUpRequest = UserFixture.createRequestForUserSignUp();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp_success() {
        // given
        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByNickname(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        Account newAccount = Account.builder()
            .email("new@example.com")
            .password("encodedPassword")
            .role(UserRole.BASIC)
            .status(AccountStatus.ACTIVE)
            .build();

        User newUser = User.builder()
            .account(newAccount)
            .nickname("newUser")
            .phoneNumber("01087654321")
            .build();

        when(accountRepository.save(any(Account.class))).thenReturn(newAccount);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // when
        UserSignUpResponse response = authService.userSignUp(validSignUpRequest);

        // then
        assertAll(
            () -> assertThat(response).isNotNull(),
            () -> assertThat(response.email()).isEqualTo("new@example.com"),
            () -> assertThat(response.nickname()).isEqualTo("newUser")
        );
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 이메일")
    void signUp_ExistEmail() {
        // given
        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.of(mockAccount));

        // when then
        assertThrows(BusinessException.class, () -> authService.userSignUp(validSignUpRequest));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 닉네임")
    void signUp_ExistNickname() {
        // given
        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByNickname(anyString())).thenReturn(Optional.of(mockUser));

        // when then
        assertThrows(BusinessException.class, () -> authService.userSignUp(validSignUpRequest));
    }

}
