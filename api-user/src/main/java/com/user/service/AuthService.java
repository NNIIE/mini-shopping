package com.user.service;

import com.storage.account.Account;
import com.storage.account.AccountRepository;
import com.storage.enums.AccountStatus;
import com.storage.enums.DeviceType;
import com.storage.enums.UserRole;
import com.storage.token.Token;
import com.storage.user.User;
import com.storage.user.UserRepository;
import com.user.global.exception.ConflictException;
import com.user.global.exception.ErrorCode;
import com.user.global.exception.NotFoundException;
import com.user.security.TokenService;
import com.user.web.request.ReissueTokenRequest;
import com.user.web.request.UserSignInRequest;
import com.user.web.request.UserSignUpRequest;
import com.user.web.response.UserSignUpResponse;
import com.user.web.response.UserTokenPairDto;
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
        checkDuplicateEmail(request.getEmail());
        checkDuplicateNickname(request.getNickname());
        final Account savedAccount = createAccount(request);
        final User savedUser = createUser(request, savedAccount);

        return new UserSignUpResponse(
            savedUser.getId(),
            savedUser.getAccount().getEmail(),
            savedUser.getNickname()
        );
    }

    @Transactional
    public UserTokenPairDto signIn(final UserSignInRequest request) {
        final User user = getUser(request);
        final Instant now = Instant.now();
        final UserTokenPairDto tokenResponse = tokenService.createTokenPair(user.getId(), now);

        tokenService.issueRefreshToken(
            user,
            tokenResponse.refreshToken(),
            DeviceType.MAC,
            "123.456.789",
            now
        );

        return tokenResponse;
    }

    @Transactional
    public UserTokenPairDto reissueAccessToken(final ReissueTokenRequest request) {
        final Token originRefreshToken = tokenService.validateAndGetRefreshToken(request.getRefreshToken());
        final User user = originRefreshToken.getUser();
        final Instant now = Instant.now();
        final UserTokenPairDto tokenResponse = tokenService.createTokenPair(user.getId(), now);

        tokenService.rotateRefreshToken(
            originRefreshToken,
            tokenResponse.refreshToken(),
            DeviceType.MAC,
            "123.456.789",
            now
        );

        return tokenResponse;
    }

    private void checkDuplicateEmail(final String email) {
        accountRepository.findByEmail(email).ifPresent(account -> {
            throw new ConflictException(ErrorCode.EMAIL_CONFLICT);
        });
    }

    private void checkDuplicateNickname(final String nickname) {
        userRepository.findByNickname(nickname).ifPresent(user -> {
            throw new ConflictException(ErrorCode.NICKNAME_CONFLICT);
        });
    }

    private Account createAccount(final UserSignUpRequest request) {
        final Account account = Account.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(UserRole.BASIC)
            .status(AccountStatus.ACTIVE)
            .build();

        return accountRepository.save(account);
    }

    private User createUser(final UserSignUpRequest request, final Account savedAccount) {
        final User signUpUser = User.builder()
            .account(savedAccount)
            .nickname(request.getNickname())
            .phoneNumber(request.getPhoneNumber())
            .build();

        return userRepository.save(signUpUser);
    }

    private User getUser(final UserSignInRequest request) {
        final User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.verifyPassword(request.getPassword(), user.getAccount().getPassword())) {
            throw new NotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        return user;
    }

}
