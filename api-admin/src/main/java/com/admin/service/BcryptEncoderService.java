package com.admin.service;

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

    /**
     * 주어진 평문 비밀번호가 저장된 해시 비밀번호와 일치하는지 확인합니다.
     *
     * @param requestPassword 사용자가 입력한 평문 비밀번호
     * @param originPassword 저장된 해시 비밀번호
     * @return 비밀번호가 일치하면 true, 그렇지 않으면 false
     */
    @Override
    public boolean verifyPassword(final String requestPassword, final String originPassword) {
        return bCryptPasswordEncoder.matches(requestPassword, originPassword);
    }

}

