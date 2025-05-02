package com.shopping.web.controller;

import com.shopping.service.UserService;
import com.shopping.web.request.UserSignUpRequest;
import com.shopping.web.response.UserSignUpResponse;
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
public class UserController {

    private final UserService UserService;

    @PostMapping("")
    public ResponseEntity<UserSignUpResponse> userSignUp(@RequestBody @Valid final UserSignUpRequest request) {
        final UserSignUpResponse userSignUpResponse = UserService.userSignUp(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userSignUpResponse);
    }

}
