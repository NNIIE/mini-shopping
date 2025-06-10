package com.user.web.response.auth;

public record UserSignUpResponse(
    Long id,
    String email,
    String nickname
) {
}

