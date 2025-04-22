package com.shopping.controller;

import com.shopping.model.request.UserSignUpRequest;
import com.shopping.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody @Valid final UserSignUpRequest request) {
        userService.signUp(request);
    }

}
