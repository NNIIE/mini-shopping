package com.shopping.service;

import com.shopping.exception.ApiException;
import com.shopping.exception.ErrorCode;
import com.shopping.model.entity.User;
import com.shopping.model.request.UserSignUpRequest;
import com.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(final UserSignUpRequest request) {
        existsByEmail(request.getEmail());
        existsByNickname(request.getNickname());

        final User signUpUser = User.createSignUpUser(
            request.getNickname(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            request.getUserRole()
        );

        userRepository.save(signUpUser);
    }

    private void existsByEmail(final String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ApiException(ErrorCode.EMAIL_CONFLICT);
        }
    }

    private void existsByNickname(final String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new ApiException(ErrorCode.NICKNAME_CONFLICT);
        }
    }

}
