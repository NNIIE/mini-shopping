package com.user.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes("AccessToken",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                )
            )
            .addSecurityItem(new SecurityRequirement().addList("AccessToken"))  // 모든 API에 기본 적용
            .info(apiInfo());
    }

    /**
     * OpenAPI 문서에 사용할 API 메타데이터 정보를 생성합니다.
     *
     * @return API의 제목, 버전, 설명이 포함된 Info 객체
     */
    private Info apiInfo() {
        return new Info()
            .title("Mini Shopping User Api Swagger")
            .version("1.0")
            .description("User API Documentation");
    }

}

