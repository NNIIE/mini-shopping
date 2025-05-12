package com.admin.service;

import com.admin.global.exception.ConflictException;
import com.admin.global.exception.ErrorCode;
import com.admin.web.request.AdminSignUpRequest;
import com.admin.web.response.AdminSignUpResponse;
import com.storage.account.Account;
import com.storage.account.AccountRepository;
import com.storage.admin.Admin;
import com.storage.admin.AdminRepository;
import com.storage.enums.AccountStatus;
import com.storage.enums.UserRole;
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
        final Account savedAccount = createAccount(request);
        final Admin savedAdmin = createAdmin(savedAccount);

        return new AdminSignUpResponse(
            savedAdmin.getId(),
            savedAdmin.getAccount().getEmail()
        );
    }

    private void checkDuplicateEmail(final String email) {
        accountRepository.findByEmail(email).ifPresent(account -> {
            throw new ConflictException(ErrorCode.EMAIL_CONFLICT);
        });
    }

    private Account createAccount(final AdminSignUpRequest request) {
        final Account account = Account.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(UserRole.ADMIN)
            .status(AccountStatus.ACTIVE)
            .build();

        return accountRepository.save(account);
    }

    private Admin createAdmin(final Account savedAccount) {
        final Admin admin = new Admin(savedAccount);
        return adminRepository.save(admin);
    }

}
