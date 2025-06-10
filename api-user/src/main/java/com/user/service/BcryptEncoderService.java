package com.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BcryptEncoderService implements PasswordEncoder {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String encode(final String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    @Override
    public boolean verifyPassword(final String requestPassword, final String originPassword) {
        return bCryptPasswordEncoder.matches(requestPassword, originPassword);
    }

}
