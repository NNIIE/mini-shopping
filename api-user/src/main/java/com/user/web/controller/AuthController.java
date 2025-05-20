package com.user.web.controller;

import com.user.service.AuthService;
import com.user.web.request.ReissueTokenRequest;
import com.user.web.request.UserSignInRequest;
import com.user.web.request.UserSignUpRequest;
import com.user.web.response.UserTokenDto;
import com.user.web.response.UserSignUpResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")
    public ResponseEntity<UserSignUpResponse> userSignUp(
        @RequestBody @Valid final UserSignUpRequest userSignUpRequest
    ) {
        final UserSignUpResponse response = authService.userSignUp(userSignUpRequest);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @PostMapping("/signIn")
    public ResponseEntity<UserTokenDto> signIn(
        @RequestBody @Valid final UserSignInRequest userSignInRequest
    ) {
        UserTokenDto response = authService.signIn(userSignInRequest);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }

    @PostMapping("/reissueToken")
    public ResponseEntity<UserTokenDto> reissueAccessToken(
        @RequestBody @Valid final ReissueTokenRequest request
    ) {
        final UserTokenDto response = authService.reissueAccessToken(request);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }

}
