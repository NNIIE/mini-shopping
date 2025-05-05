package com.admin.unit.service;

import com.admin.fixture.AdminFixture;
import com.admin.global.exception.ConflictException;
import com.admin.global.exception.ErrorCode;
import com.admin.service.AuthService;
import com.admin.service.PasswordEncoder;
import com.admin.web.request.AdminSignUpRequest;
import com.storage.account.Account;
import com.storage.account.AccountRepository;
import com.storage.admin.Admin;
import com.storage.admin.AdminRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class AuthServiceSignUpUnitTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccountRepository accountRepository;

    @Test
    @DisplayName("관리자 등록 - 성공")
    void adminSignUpSuccess() {
        // Given
        AdminSignUpRequest request = AdminFixture.createRequestForAdminSignUp();
        when(accountRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // When
        authService.adminSignUp(request);
        ArgumentCaptor<Admin> adminCaptor = ArgumentCaptor.forClass(Admin.class);
        verify(adminRepository, times(1)).save(adminCaptor.capture());
        Admin savedAdmin = adminCaptor.getValue();


    }

    @Test
    @DisplayName("관리자 등록 - 중복 이메일")
    void adminSignUpExistsEmail() {
        // Given
        AdminSignUpRequest request = AdminFixture.createRequestForAdminSignUp();
        Account existingAccount = AdminFixture.createAdminAccount();
        when(accountRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingAccount));

        // When & Then
        assertThatThrownBy(() -> authService.adminSignUp(request))
            .isInstanceOf(ConflictException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.EMAIL_CONFLICT);
    }

}
