package com.admin.service;

import com.admin.domain.AccountFactory;
import com.admin.exception.BusinessException;
import com.admin.exception.ErrorCode;
import com.admin.web.request.auth.AdminSignUpRequest;
import com.admin.web.response.auth.AdminSignUpResponse;
import com.relation.account.Account;
import com.relation.account.AccountRepository;
import com.relation.admin.Admin;
import com.relation.admin.AdminRepository;
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

    private Admin registerAdmin(final Account savedAccount) {
        final Admin admin = new Admin(savedAccount);
        return adminRepository.save(admin);
    }

}
