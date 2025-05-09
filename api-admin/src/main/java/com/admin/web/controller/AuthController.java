package com.admin.web.controller;

import com.admin.service.AuthService;
import com.admin.web.request.AdminSignUpRequest;
import com.admin.web.response.AdminSignUpDto;
import com.support.response.ApiResponse;
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

    @PostMapping("")
    public ResponseEntity<ApiResponse<AdminSignUpDto>> adminSignUp(@RequestBody @Valid final AdminSignUpRequest request) {
        final AdminSignUpDto dto = authService.adminSignUp(request);
        final ApiResponse<AdminSignUpDto> responseBody = ApiResponse.success(dto);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(responseBody);
    }

}
