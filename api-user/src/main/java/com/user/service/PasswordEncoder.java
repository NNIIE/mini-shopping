package com.user.service;

public interface PasswordEncoder {

    String encode(String password);

    /**
 * 입력된 비밀번호가 원본 비밀번호와 일치하는지 확인합니다.
 *
 * @param requestPassword 사용자가 입력한 비밀번호
 * @param originPassword 저장된(인코딩된) 원본 비밀번호
 * @return 비밀번호가 일치하면 true, 그렇지 않으면 false
 */
boolean verifyPassword(String requestPassword, String originPassword);

}

