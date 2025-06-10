package com.admin.service;

import com.admin.domain.AccountFactory;
import com.admin.exception.BusinessException;
import com.admin.exception.ErrorCode;
import com.admin.web.request.auth.AdminSignUpRequest;
import com.admin.web.response.auth.AdminSignUpResponse;
import com.storage.account.Account;
import com.storage.account.AccountRepository;
import com.storage.admin.Admin;
import com.storage.admin.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final AdminRepository adminRepository;

    @Transactional
    public AdminSignUpResponse adminSignUp(final AdminSignUpRequest request) {
        checkDuplicateEmail(request.getEmail());
        final Account savedAccount = registerAccount(request);
        final Admin savedAdmin = registerAdmin(savedAccount);

        return new AdminSignUpResponse(
            savedAdmin.getId(),
            savedAdmin.getAccount().getEmail()
        );
    }

    private void checkDuplicateEmail(final String email) {
        accountRepository.findByEmail(email).ifPresent(account -> {
            throw new BusinessException(ErrorCode.EMAIL_CONFLICT);
        });
    }

    private Account registerAccount(final AdminSignUpRequest request) {
        final Account account = AccountFactory.createAdminAccountForSignUp(
            request.getEmail(),
            passwordEncoder.encode(request.getPassword())
        );

        return accountRepository.save(account);
    }

    /**
     * 저장된 계정 정보를 기반으로 새로운 관리자 엔티티를 생성하고 저장합니다.
     *
     * @param savedAccount 관리자와 연결할 저장된 계정 엔티티
     * @return 저장된 관리자 엔티티
     */
    private Admin registerAdmin(final Account savedAccount) {
        final Admin admin = new Admin(savedAccount);
        return adminRepository.save(admin);
    }

}

