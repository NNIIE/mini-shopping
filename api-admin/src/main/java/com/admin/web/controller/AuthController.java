package com.admin.web.controller;

import com.admin.security.AuthenticationSessionManager;
import com.admin.service.AuthService;
import com.admin.web.request.auth.AdminSignInRequest;
import com.admin.web.request.auth.AdminSignUpRequest;
import com.admin.web.response.auth.AdminSignUpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "Auth", description = "Admin Authentication API")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationSessionManager authenticationSessionManager;

    @PostMapping("/signUp")
    @Operation(summary = "회원가입")
    public ResponseEntity<AdminSignUpResponse> adminSignUp(@RequestBody @Valid final AdminSignUpRequest request) {
        final AdminSignUpResponse response = authService.adminSignUp(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    /**
     * 관리자가 이메일과 비밀번호로 로그인하여 세션을 생성합니다.
     *
     * @param request 로그인 요청 정보가 담긴 객체
     * @param httpRequest HTTP 요청 객체
     * @return 생성된 세션 ID를 포함한 응답
     */
    @PostMapping("/signIn")
    @Operation(summary = "로그인")
    public ResponseEntity<String> adminSignIn(
        @RequestBody @Valid final AdminSignInRequest request,
        final HttpServletRequest httpRequest
    ) {
        final String sessionId = authenticationSessionManager.authenticateAdmin(
            request.getEmail(),
            request.getPassword(),
            httpRequest
        );

        return ResponseEntity.ok(sessionId);
    }

}

