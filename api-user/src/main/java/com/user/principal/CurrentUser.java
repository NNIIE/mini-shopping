package com.user.principal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal(expression = "user")
public @interface CurrentUser {
}

/*
  1. JwtFilter: 토큰 추출 및 검증
  2. TokenService: 토큰에서 사용자 ID 추출
  3. CustomUserDetailService: ID로 사용자 정보 로드
  4. CustomUserDetails: 객체 생성 (User 참조 포함)
  5. Authentication 객체 생성 후 SecurityContextHolder 에 저장
  6. @AuthenticationPrincipal: SecurityContext 에서 인증 정보 추출
  7. expression="user": CustomUserDetails.getUser() 호출하여 User 객체 획득
  7. User 객체를 컨트롤러 메서드 파라미터로 주입
 */

