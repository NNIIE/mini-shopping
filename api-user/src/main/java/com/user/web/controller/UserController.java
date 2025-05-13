package com.user.web.controller;

import com.storage.user.User;
import com.user.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @GetMapping("/login-test")
    public HashMap<String, String> test(@CurrentUser final User user) {
        HashMap<String, String> map = new HashMap<>();
        map.put("nickname", user.getNickname());
        map.put("phoneNumber", user.getPhoneNumber());
        map.put("userId", user.getId().toString());
        map.put("accountId", user.getAccount().getId().toString());
        map.put("email", user.getAccount().getEmail());
        map.put("role", user.getAccount().getRole().toString());
        map.put("status", user.getAccount().getStatus().name());

        return map;
    }

}
