package com.user.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/actuator")
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "ok";
    }

}
