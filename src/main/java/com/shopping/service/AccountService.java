package com.shopping.service;

import com.shopping.global.enums.AccountStatus;
import com.shopping.global.enums.UserRole;
import com.shopping.global.exception.ErrorCode;
import com.shopping.global.exception.NotFoundException;
import com.shopping.storage.account.Account;
import com.shopping.storage.account.AccountRepository;
import com.shopping.web.response.AccountFindResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public AccountFindResponse findAccountByEmail(final String email) {
        return accountRepository.findByEmail(email)
            .map(account -> new AccountFindResponse(
                account.getId(),
                account.getEmail(),
                account.getRole(),
                account.getStatus()
            ))
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public Account createAdminAccount(final String email, final String password) {
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
        final Account account = Account.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .role(UserRole.BASIC)
            .status(AccountStatus.ACTIVE)
            .build();

        return accountRepository.save(account);
    }

}
