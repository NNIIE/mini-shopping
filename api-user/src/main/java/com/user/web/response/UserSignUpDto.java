package com.user.web.response;

public record UserSignUpDto(
    Long id,
    String email,
    String nickname
) {
}
