package com.user.service;

import com.relation.account.Account;
import com.relation.account.AccountRepository;
import com.relation.user.User;
import com.relation.user.UserRepository;
import com.user.domain.account.AccountFactory;
import com.user.domain.user.UserFactory;
import com.user.exception.BusinessException;
import com.user.exception.ErrorCode;
import com.user.web.request.auth.ReissueTokenRequest;
import com.user.web.request.auth.UserSignInRequest;
import com.user.web.request.auth.UserSignUpRequest;
import com.user.web.response.auth.UserSignUpResponse;
import com.user.web.response.auth.UserTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Transactional
    public UserSignUpResponse userSignUp(final UserSignUpRequest request) {
        accountRepository.findByEmail(request.getEmail()).ifPresent(account -> {
            throw new BusinessException(ErrorCode.EMAIL_CONFLICT);
        });

        userRepository.findByNickname(request.getNickname()).ifPresent(user -> {
            throw new BusinessException(ErrorCode.NICKNAME_CONFLICT);
        });

        final Account savedAccount = registerAccount(request);
        final User savedUser = registerUser(request, savedAccount);

        return new UserSignUpResponse(
            savedUser.getId(),
            savedUser.getAccount().getEmail(),
            savedUser.getNickname()
        );
    }

    @Transactional
    public UserTokenDto signIn(final UserSignInRequest request) {
        final User user = authenticateUser(request);
        final Instant now = Instant.now();
        final UserTokenDto tokenResponse = tokenService.createAccessAndRefreshToken(user.getId(), now);

        user.setRefreshToken(tokenResponse.refreshToken());

        return tokenResponse;
    }

    @Transactional
    public String reissueAccessToken(final ReissueTokenRequest request) {
        final Long userId = tokenService.validateTokenAndGetUserId(request.getRefreshToken());

        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!user.getRefreshToken().equals(request.getRefreshToken())) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        return tokenService.createAccessToken(user.getId(), Instant.now());
    }

    public User authenticateUser(final UserSignInRequest request) {
        final User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.verifyPassword(request.getPassword(), user.getAccount().getPassword())) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        return user;
    }

    private Account registerAccount(final UserSignUpRequest request) {
        final Account account = AccountFactory.createUserAccountForSignUp(
            request.getEmail(),
            passwordEncoder.encode(request.getPassword())
        );

        return accountRepository.save(account);
    }

    private User registerUser(final UserSignUpRequest request, final Account savedAccount) {
        final User signUpUser = UserFactory.createUserForSignUp(
            savedAccount,
            request.getNickname(),
            request.getPhoneNumber()
        );

        return userRepository.save(signUpUser);
    }

}
