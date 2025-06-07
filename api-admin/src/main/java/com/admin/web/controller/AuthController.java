package com.admin.web.controller;

import com.admin.security.AuthenticationSessionManager;
import com.admin.service.AuthService;
import com.admin.web.request.auth.AdminSignInRequest;
import com.admin.web.request.auth.AdminSignUpRequest;
import com.admin.web.response.auth.AdminSignUpResponse;
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
public class AuthController {

    private final AuthService authService;
    private final AuthenticationSessionManager authenticationSessionManager;

    @PostMapping("/signUp")
    public ResponseEntity<AdminSignUpResponse> adminSignUp(@RequestBody @Valid final AdminSignUpRequest request) {
        final AdminSignUpResponse response = authService.adminSignUp(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @PostMapping("/signIn")
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
