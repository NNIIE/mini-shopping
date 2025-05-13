package com.user.web.response;

public record UserTokenPairDto(
    String accessToken,
    String refreshToken
) {
}
