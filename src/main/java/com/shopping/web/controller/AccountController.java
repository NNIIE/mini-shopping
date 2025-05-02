package com.shopping.web.controller;

import com.shopping.service.AccountService;
import com.shopping.web.response.AccountFindResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("")
    public ResponseEntity<AccountFindResponse> findAccountByEmail(@RequestParam final String email) {
        final AccountFindResponse account = accountService.findAccountByEmail(email);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(account);
    }

}
