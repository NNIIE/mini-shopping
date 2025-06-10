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

    /**
     * 액세스 토큰을 재발급합니다.
     *
     * 클라이언트가 유효한 리프레시 토큰을 제공하면 새로운 액세스 토큰을 반환합니다.
     *
     * @param request 토큰 재발급 요청 정보
     * @return 재발급된 액세스 토큰 문자열이 포함된 응답
     */
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

