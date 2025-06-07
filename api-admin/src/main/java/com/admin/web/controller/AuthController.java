package com.admin.web.controller;

import com.admin.service.AuthService;
import com.admin.web.request.AdminSignUpRequest;
import com.admin.web.response.AdminSignUpResponse;
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

    @PostMapping("/signUp")
    public ResponseEntity<AdminSignUpResponse> adminSignUp(@RequestBody @Valid final AdminSignUpRequest request) {
        final AdminSignUpResponse response = authService.adminSignUp(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

}
