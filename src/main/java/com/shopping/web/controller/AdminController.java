package com.shopping.web.controller;

import com.shopping.service.AdminService;
import com.shopping.web.request.AdminSignUpRequest;
import com.shopping.web.response.AdminSignUpResponse;
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
public class AdminController {

    private final AdminService adminService;

    @PostMapping("")
    public ResponseEntity<AdminSignUpResponse> adminSignUp(@RequestBody @Valid final AdminSignUpRequest request) {
        final AdminSignUpResponse adminSignUpResponse = adminService.adminSignUp(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(adminSignUpResponse);
    }

}
