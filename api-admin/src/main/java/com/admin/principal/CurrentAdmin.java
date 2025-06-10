package com.admin.principal;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal(expression = "admin")
public @interface CurrentAdmin {
}

/*
  1. 로그인 시 Spring Security가 세션에 인증 정보 저장
  2. 이후 요청에서 세션 ID(JSESSIONID 쿠키)로 인증 정보 복원
  3. SecurityContextHolder에 Authentication 객체 설정
  4. @AuthenticationPrincipal이 SecurityContext에서 인증 정보 추출
  5. expression="admin"으로 CustomAdminDetails.getAdmin() 호출
  6. Admin 객체를 컨트롤러 메서드 파라미터로 주입
 */
