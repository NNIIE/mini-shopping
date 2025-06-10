package com.user.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/actuator")
public class HealthController {

    /**
     * 서비스의 상태를 확인하기 위한 헬스 체크 엔드포인트를 제공합니다.
     *
     * @return 서비스가 정상적으로 동작 중임을 나타내는 문자열 "ok"
     */
    @GetMapping("/health")
    public String health() {
        return "ok";
    }

}

