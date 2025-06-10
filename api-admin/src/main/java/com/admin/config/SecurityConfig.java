package com.admin.config;

import com.admin.security.CustomAdminDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAdminDetailService customAdminDetailService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(customAdminDetailService).passwordEncoder(bCryptPasswordEncoder());
        return builder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 필요시 세션 생성 (기본값)
            )
            .authorizeHttpRequests(this::configureAuthorizeRequests)
            .formLogin(AbstractHttpConfigurer::disable)
            .userDetailsService(customAdminDetailService);

        return http.build();
    }

    /**
     * HTTP 요청 경로별로 접근 권한을 설정합니다.
     *
     * API 문서, 헬스 체크, 인증 관련 엔드포인트는 모두 허용하며,
     * `/admin/**` 경로는 ADMIN 역할이 필요합니다.
     * 그 외 모든 요청은 인증이 필요합니다.
     *
     * @param auth HTTP 요청 권한 설정을 위한 레지스트리
     */
    private void configureAuthorizeRequests(final AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth.requestMatchers(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/admin/actuator/health",
                "/admin/signIn",
                "/admin/signUp"
            ).permitAll()
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated();
    }

}

