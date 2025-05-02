package com.shopping.service;

import com.shopping.global.exception.ConflictException;
import com.shopping.global.exception.ErrorCode;
import com.shopping.storage.account.Account;
import com.shopping.storage.user.User;
import com.shopping.storage.user.UserRepository;
import com.shopping.web.request.UserSignUpRequest;
import com.shopping.web.response.UserSignUpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AccountService accountService;
    private final UserRepository userRepository;

    @Transactional
    public UserSignUpResponse userSignUp(final UserSignUpRequest request) {
        userRepository.findByNickname(request.getNickname()).ifPresent(user -> {
            throw new ConflictException(ErrorCode.NICKNAME_CONFLICT);
        });

        final Account createAccount = accountService.createUserAccount(request.getEmail(), request.getPassword());
        final User signUpUser = createUser(request, createAccount);

        return new UserSignUpResponse(
            signUpUser.getId(),
            signUpUser.getAccount().getEmail(),
            signUpUser.getNickname()
        );
    }

    private User createUser(final UserSignUpRequest request, final Account savedAccount) {
        final User signUpUser = User.builder()
            .account(savedAccount)
            .nickname(request.getNickname())
            .phoneNumber(request.getPhoneNumber())
            .build();

        return userRepository.save(signUpUser);
    }

}
