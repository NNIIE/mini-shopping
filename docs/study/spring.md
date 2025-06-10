- [@SpringBootApplication](#springBootApplication)
- [@Configuration](#configuration)
- [@AutoConfiguration](#autoConfiguration)
- [@ConfigurationProperties](#configurationProperties)
- [@EnableWebSecurity](#enableWebSecurity)


<br>

# @SpringBootApplication
### 등장배경
- Spring XML 기반 설정의 복잡함을 해결하기 위해 등장
- Spring Boot 1.2부터 등장한 3가지 어노테이션을 한번에 활성화 하는 메타 어노테이션
  - @SpringBootConfiguration
  - @EnableAutoConfiguration
  - @ComponentScan

### 구조
#### @SpringBootConfiguration
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
public @interface SpringBootConfiguration {
    @AliasFor(annotation = Configuration.class)
    boolean proxyBeanMethods() default true;
}
```
- @Configuration을 확장
- Spring Boot 기능에서 어플리케이션의 구성을 찾는데 사용
#### @EnableAutoConfiguration
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {
    String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";
    Class<?>[] exclude() default {};
    String[] excludeName() default {};
}
```
- Spring Boot의 자동 설정을 활성화하는 어노테이션
- 클래스패스에 있는 라이브러리와 사용자 정의 빈을 기반으로 어플리케이션 구성을 자동으로 설정
- @AutoConfigurationPackage: 어노테이션이 위치한 패키지를 자동 구성의 기본 패키지로 등록
#### @ComponentScan
- @Component, @Service, @Repository, @Controller 등의 어노테이션이 붙은 클래스를 스캔하여 Spring Bean으로 등록

### 동작 원리
Spring Boot 어플리케이션이 시작될 때 발생하는 과정
1. **어플리케이션 시작 및 환경 준비**
   - 어플리케이션 유형 감지
   - 외부설정 로드 (application.yml, 환경변수 등)
2. **어플리케이션 생성 및 준비**
   - 어플리케이션 유형에 맞는 ApplicationContext 생성
3. **자동 구성 적용**
   - @EnableAutoConfiguration 작동
   - @ComponentScan으로 사용자 정의 빈 등록
4. **내장서버 시작**
   - 웹 어플리케이션일 경우
     - 내장 서버 초기화 (Tomcat, Jetty 등)
     - 서블릿, 필터, 리스너 등록
     - 웹서버 시작 및 DispatcherServlet 등록 및 초기화
5. **어플리케이션 시작 완료**
   - 어플리케이션 시작 완료 이벤트(ApplicationReadyEvent) 발행


<br>


# @Configuration
- @Configuration 클래스는 Spring 컨테이너에 의해 CGLIB 프록시로 감싸진다
- @Bean 메서드 간의 호출에서 싱글톤을 보장하기 위해 CGLIB 프록시 활용
  - @Bean 메서드 호출을 가로채서 스프링 컨테이너에 등록된 빈을 반환
  - @Bean 메서드가 여러 번 호출되더라도 항상 동일한 인스턴스를 반환하게 되어 싱글톤 스코프가 보장
- @Configuration 대신 @Component 와 @Bean 사용시 
  - @Bean 메서드가 호출될 때마다 새로운 인스턴스가 생성되어 싱글톤 스코프가 보장되지 않음

<br>


# @AutoConfiguration
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore
@AutoConfigureAfter
public @interface AutoConfiguration {
    Class<?>[] before() default {};
    Class<?>[] after() default {};
    String[] name() default {};
    // ...
}
```
-  XML 파일이나 Java Config 클래스를 통해 모든 빈과 설정을 직접 정의해야 했던걸 개선
- 개발자가 명시적으로 빈을 설정하지 않아도 스프링이 클래스패스와 환경을 기반으로 자동으로 적절한 설정을 제공
- 


<br>


# @ConfigurationProperties
```java
@Target({TYPE, METHOD})
@Retention(RUNTIME)
@Documented
@Indexed
public @interface ConfigurationProperties {
    String prefix() default "";
    String value() default "";
    boolean ignoreUnknownFields() default true;
    boolean ignoreInvalidFields() default false;
}

```
- 설정 프로퍼티를 타입 세이프하게 매핑하기 위해 제공
  - @Value를 통해 주입하거나 Environment 객체 직접 참조를 개선
- @ConfigurationProperties 클래스가 실제 빈으로 등록되기위한 방법
  - @EnableConfigurationProperties
  - @ConfigurationPropertiesScan
  - @Component/@Configuration + @ConfigurationProperties


<br>


# @Enable~
- 메타어노테이션
- 내부적으로 @Import를 사용하여 필요한 설정 클래스들을 스프링 컨텍스트에 로드
- 해당기술을 사용할 수 있는 추상화된 편의를 제공
- @Configuration을 제외하고 사용할 수 있지만 권장하지 않음
  - CGLIB 프록시를 통한 빈 메서드 호출 최적화 되지 않음


<br>





