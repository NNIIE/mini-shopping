package com.admin.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .components(new Components())
            .info(apiInfo());
    }

    /**
     * OpenAPI 문서의 제목, 버전, 설명 정보를 포함하는 Info 객체를 생성합니다.
     *
     * @return API의 메타데이터가 설정된 Info 객체
     */
    private Info apiInfo() {
        return new Info()
            .title("Mini Shopping Admin Api Swagger")
            .version("1.0")
            .description("Admin API Documentation");
    }

}

