package com.user.web.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ReissueTokenRequest {

    @NotBlank
    private String refreshToken;

}
