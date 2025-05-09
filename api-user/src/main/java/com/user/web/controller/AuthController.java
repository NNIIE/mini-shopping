package com.user.web.controller;

import com.support.response.ApiResponse;
import com.user.service.AuthService;
import com.user.web.request.UserSignUpRequest;
import com.user.web.response.UserSignUpDto;
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
@RequestMapping("/user")
public class AuthController {

    private final AuthService authService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<UserSignUpDto>> userSignUp(@RequestBody @Valid final UserSignUpRequest userSignUpRequest) {
        final UserSignUpDto dto = authService.userSignUp(userSignUpRequest);
        final ApiResponse<UserSignUpDto> responseBody = ApiResponse.success(dto);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(responseBody);
    }

}
