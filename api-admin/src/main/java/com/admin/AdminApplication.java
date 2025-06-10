package com.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.storage")
@EnableJpaRepositories("com.storage")
@ComponentScan(basePackages = {
    "com.admin",
    "com.storage",
    "com.support"
})
public class AdminApplication {

    /**
     * Spring Boot 애플리케이션을 시작합니다.
     *
     * @param args 커맨드라인 인수
     */
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
