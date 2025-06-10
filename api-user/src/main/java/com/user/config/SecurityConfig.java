package com.user.config;

import com.user.security.CustomUserDetailService;
import com.user.jwt.JwtFilter;
import com.user.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenService tokenService;
    private final CustomUserDetailService customUserDetailService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Spring: Configuration + SecurityFilterChain Bean 을 정의하면 @EnableWebSecurity 사용 불필요
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(this::configureAuthorizeRequests)
            .sessionManagement(this::configureSessionManagement)
            .addFilterBefore(
                new JwtFilter(tokenService, customUserDetailService),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    private void configureAuthorizeRequests(final AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth.requestMatchers(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/user/signUp",
                "/user/signIn",
                "/user/reissueToken",
                "/user/actuator/health"
            ).permitAll()
            .anyRequest()
            .authenticated();
    }

    /**
     * 세션 관리 정책을 무상태(Stateless)로 설정합니다.
     *
     * 이 설정은 서버가 세션을 생성하거나 저장하지 않도록 하여, JWT 기반 인증과 같은 무상태 인증 방식을 지원합니다.
     */
    private void configureSessionManagement(final SessionManagementConfigurer<HttpSecurity> session) {
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}

