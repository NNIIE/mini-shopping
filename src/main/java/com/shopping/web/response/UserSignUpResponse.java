package com.shopping.web.response;

public record UserSignUpResponse(
    Long id,
    String email,
    String nickname
) {
}
