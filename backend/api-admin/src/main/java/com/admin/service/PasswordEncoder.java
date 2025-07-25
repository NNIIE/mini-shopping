package com.admin.service;

public interface PasswordEncoder {

    String encode(String password);

    boolean verifyPassword(String requestPassword, String originPassword);

}
