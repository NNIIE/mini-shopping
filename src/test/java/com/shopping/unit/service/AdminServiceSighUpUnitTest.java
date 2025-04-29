package com.shopping.unit.service;

import com.shopping.fixture.UserFixture;
import com.shopping.global.exception.ConflictException;
import com.shopping.global.exception.ErrorCode;
import com.shopping.service.AccountService;
import com.shopping.service.AdminService;
import com.shopping.storage.entity.Account;
import com.shopping.storage.entity.Admin;
import com.shopping.storage.repository.AdminRepository;
import com.shopping.web.request.AdminSignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class AdminServiceSighUpUnitTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private AccountService accountService;

    @Mock
    private AdminRepository adminRepository;

    @Test
    @DisplayName("관리자 등록 - 성공")
    void adminSignUpSuccess() {
        // Given
        AdminSignUpRequest request = UserFixture.createRequestForAdminSignUp();
        Account account = UserFixture.createAdminAccount();
        when(accountService.createAdminAccount(request.getEmail(), request.getPassword())).thenReturn(account);
        when(adminRepository.save(any(Admin.class))).thenReturn(new Admin(account));

        // When
        adminService.adminSignUp(request);
        ArgumentCaptor<Admin> adminCaptor = ArgumentCaptor.forClass(Admin.class);
        verify(adminRepository, times(1)).save(adminCaptor.capture());
        Admin savedAdmin = adminCaptor.getValue();

        // Then
        assertAll(
            () -> assertThat(savedAdmin.getAccount()).isEqualTo(account)
        );
    }

    @Test
    @DisplayName("관리자 등록 - 중복 이메일")
    void adminSignUpExistsEmail() {
        // Given
        AdminSignUpRequest request = UserFixture.createRequestForAdminSignUp();
        when(accountService.createAdminAccount(request.getEmail(), request.getPassword()))
            .thenThrow(new ConflictException(ErrorCode.EMAIL_CONFLICT));

        // When & Then
        assertThatThrownBy(() -> adminService.adminSignUp(request))
            .isInstanceOf(ConflictException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.EMAIL_CONFLICT);
    }

}
