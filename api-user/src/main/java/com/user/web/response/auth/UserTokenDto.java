package com.user.web.response.auth;

public record UserTokenDto(
    String accessToken,
    String refreshToken
) {
}

