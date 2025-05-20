package com.user.web.response;

public record UserTokenDto(
    String accessToken,
    String refreshToken
) {
}
