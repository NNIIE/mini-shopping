package com.user.web.controller;

import com.user.service.AuthService;
import com.user.web.request.auth.ReissueTokenRequest;
import com.user.web.request.auth.UserSignInRequest;
import com.user.web.request.auth.UserSignUpRequest;
import com.user.web.response.auth.UserTokenDto;
import com.user.web.response.auth.UserSignUpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "Auth", description = "User Authentication API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")
    @Operation(summary = "회원가입")
    public ResponseEntity<UserSignUpResponse> userSignUp(
        @RequestBody @Valid final UserSignUpRequest userSignUpRequest
    ) {
        final UserSignUpResponse response = authService.userSignUp(userSignUpRequest);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @PostMapping("/signIn")
    @Operation(summary = "로그인")
    public ResponseEntity<UserTokenDto> signIn(
        @RequestBody @Valid final UserSignInRequest userSignInRequest
    ) {
        final UserTokenDto response = authService.signIn(userSignInRequest);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }

    @PostMapping("/reissueToken")
    @Operation(summary = "토큰 재발급")
    public ResponseEntity<String> reissueAccessToken(
        @RequestBody @Valid final ReissueTokenRequest request
    ) {
        final String response = authService.reissueAccessToken(request);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }

}

