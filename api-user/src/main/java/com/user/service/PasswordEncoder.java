package com.user.service;

public interface PasswordEncoder {

    String encode(String password);

    boolean verifyPassword(String requestPassword, String originPassword);

}

