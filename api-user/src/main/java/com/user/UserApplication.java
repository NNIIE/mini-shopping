package com.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.storage")
@EnableJpaRepositories("com.storage")
@ComponentScan(basePackages = {
    "com.user",
    "com.storage",
    "com.support"
})
public class UserApplication {

    /**
     * 애플리케이션의 진입점으로, Spring Boot 애플리케이션을 실행합니다.
     *
     * @param args 커맨드라인 인수
     */
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}

