package com.admin.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationSessionManager {

    private final AuthenticationManager authenticationManager;

    /****
     * 관리자 계정의 이메일과 비밀번호로 인증을 수행하고, 인증된 보안 세션을 생성하여 HTTP 세션에 저장합니다.
     *
     * @param email 인증할 관리자 이메일
     * @param password 인증할 관리자 비밀번호
     * @param request HTTP 요청 객체
     * @return 생성된 HTTP 세션의 ID
     */
    public String authenticateAdmin(
        final String email,
        final String password,
        final HttpServletRequest request
    ) {
        final Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );

        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        final HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", context);

        return session.getId();
    }

}

