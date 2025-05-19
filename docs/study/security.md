- [JWT](#jwt)
- [Spring Security](#spring-security)
- [Cookie / Session / Token / OAuth (보강)](#cookie--session--token--oauth)


<br>


# JWT
### 등장배경
stateless, scale-out 등의 문제를 해결하기 위해 등장

### 구조
Header, Payload, Signature 가 .를 구분자로 하여 JWT 토큰 1개를 이룬다.
- Header (헤더)
    - 토큰 타입과 서명 알고리즘 명시
    - Base64Url로 인코딩 됨
- Payload (내용)
    - 토큰에 담을 정보(Claim)를 포함
    - 발급자(iss), 만료시각(exp), 주제(sub) 등
- Signature (서명)
    - 인코딩 된 헤더, 페이로드, 시크릿 키, 헤더에 지정된 알고리즘을 사용하여 생성

### 동작
1. 토큰 생성
    - 서버는 사용자 인증 후 관련정보를 페이로드에 넣고 시크릿 키로 서명하여 JWT 생성
2. 클라이언트 저장
    - 클라이언트는 이 토큰을 저장(로컬 스토리지, 쿠키 등)하고 이후 요청 시 Authorization 헤더에 포함
3. 토큰 검증
    - 서버는 요청에 포함된 JWT를 받아 서명을 검증하고 페이로드의 클레임 확인
4. 리소스 접근
    - 검증에 성공하면 페이로드의 정보를 기반으로 리소스 접근 허용

### Base64Url 인코딩
JWT에서는 일반적인 Base64 인코딩과 다름 (url 에서 안전하게 사용할 수 있게)
- + -> -, / -> _, = 제거 등

### Access / Refresh
- Access Token
    - 클라이언트가 갖고있는 실제 유저의 정보가 담긴 토큰
    - 실제 서버 API 요청 시 사용
- Refresh Token
    - 새로운 Access Token을 발급해주기 위해 사용하는 토큰
    - 짧은 수명을 가지는 Access Token에게 새로운 토큰을 발급해 주기 위해 사용

### Library

### 장단점
- 장점
    - stateless: 서버가 상태를 저장하지 않음
    - 확장성: scale-out에 용이
    - 쿠키를 사용하지 않아 모바일 환경에 적합
    - 토큰에 권한정보를 포함시켜 세밀한 제어 가능
- 단점
    - 토큰 크기 증가
    - 토큰 탈취 시 만료될때 까지 악용될 수 있음 - 즉시 무효화 불가
    - 일반적인 JSW 사용 시 페이로드는 암호화 되지 않고 단지 인코딩만 됨
    - none 취약점: 일부 라이브러리는 `alg: "none"` 을 허용하여 서명없이 토큰을 검증할 수 있음

### 전략
- 짧은 만료시간 사용
- access token / refresh token 사용


<br>


# Spring Security
### 구조
#### 모듈
- spring-security-core: 인증/인가 핵심 API
- spring-security-web: Servlet 필터, 세션 관리, CSRF, CORS 지원
- spring-security-config: XML 네임스페이스 및 Java Configuration 지원

#### 주요 컴포넌트
- SecurityContextHolder: 현재 인증된 사용자 정보를 저장하는 곳
- Authentication: 인증된 사용자의 정보와 권한을 담는 객체
- AuthenticationManager: 인증 처리를 담당하는 인터페이스
- UserDetailsService: 사용자 정보를 로드하는 인터페이스
- AccessDecisionManager: 권한 부여 결정을 담당하는 인터페이스
- FilterChainProxy: 보안 필터 체인을 관리하는 특별한 필터

#### Filter Chain
모든 HTTP 요청은 FilterChainProxy(스프링 빈)를 통해 등록된 SecurityFilterChain을 순차 처리
<br>
이 구조 덕분에 특정 URL 패턴마다 다른 보안 정책을 적용하거나 커스텀 필터를 체인 중간에 삽입할 수 있다.
1. DelegatingFilterProxy가 서블릿 컨테이너에 등록 -> Spring 컨텍스트의 FilterChainProxy로 위임
2. 각 SecurityFilterChain에서 매칭 매커니즘(RequestMatcher) 으로 요청을 선별
3. 매칭된 체인의 필터(SecurityContextPersistenceFilter, UsernamePasswordAuthenticationFilter, FilterSecurityInterceptor 등) 실행

### 인증
1. 사용자가 인증 요청을 보냅니다 (폼 로그인, 토큰 등).
2. AuthenticationFilter가 요청을 가로채고 Authentication 객체를 생성
3. 이 Authentication 객체는 AuthenticationManager에게 전달
4. AuthenticationManager는 적절한 AuthenticationProvider에게 인증을 위임
5. AuthenticationProvider는 UserDetailsService를 사용해 사용자 정보를 로드
6. 비밀번호 검증 등의 인증 로직이 실행
7. 인증이 성공하면 완전히 채워진 Authentication 객체가 반환
8. SecurityContextHolder에 인증 정보가 저장

### 인가
1. 보안이 적용된 리소스에 접근 요청이 들어온다.
2. FilterSecurityInterceptor가 요청을 가로챈다.
3. SecurityContextHolder에서 현재 인증된 사용자 정보를 가져온다
4. AccessDecisionManager에게 접근 결정을 위임
5. AccessDecisionManager는 여러 AccessDecisionVoter를 사용해 접근 허용 여부를 결정
6. 접근이 허용되면 요청이 계속 진행되고 그렇지 않으면 AccessDeniedException이 발생


### AuthenticationPrincipal


<br>


# Cookie / Session / Token / OAuth


<br>