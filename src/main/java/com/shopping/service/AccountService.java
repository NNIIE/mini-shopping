package com.shopping.service;

import com.shopping.global.exception.ConflictException;
import com.shopping.global.exception.ErrorCode;
import com.shopping.service.domain.AccountStatus;
import com.shopping.service.domain.UserRole;
import com.shopping.storage.entity.Account;
import com.shopping.storage.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Account createAdminAccount(final String email, final String password) {
        validateEmail(email);

        final Account account = Account.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .role(UserRole.ADMIN)
            .status(AccountStatus.ACTIVE)
            .build();

        return accountRepository.save(account);
    }

    @Transactional
    public Account createUserAccount(final String email, final String password) {
        validateEmail(email);

        final Account account = Account.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .role(UserRole.BASIC)
            .status(AccountStatus.ACTIVE)
            .build();

        return accountRepository.save(account);
    }

    private void validateEmail(final String email) {
        accountRepository.findByEmail(email).ifPresent(account -> {
            throw new ConflictException(ErrorCode.EMAIL_CONFLICT);
        });
    }

}
