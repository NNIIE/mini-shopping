package com.admin.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/actuator")
public class HealthController {

    /****
     * 애플리케이션의 상태를 확인하는 헬스 체크 엔드포인트입니다.
     *
     * @return 서비스가 정상적으로 동작 중임을 나타내는 문자열 "ok"
     */
    @GetMapping("/health")
    public String health() {
        return "ok";
    }

}

