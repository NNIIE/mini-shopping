# 버전을 ARG로 관리하여 중앙 제어
ARG JAVA_VERSION=21
ARG GRADLE_VERSION=8.5

# 1. 빌드 스테이지 (JAR 파일 생성)
FROM gradle:${GRADLE_VERSION}-jdk${JAVA_VERSION}-alpine AS build
WORKDIR /app

# Gradle 및 의존성 파일 복사 (캐시 활용)
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./

# 각 모듈의 build.gradle 복사
COPY api-admin/build.gradle ./api-admin/
COPY api-user/build.gradle ./api-user/
COPY data/build.gradle ./data/
COPY support/build.gradle ./support/

# 의존성 캐시 빌드
RUN ./gradlew dependencies --no-daemon

# 전체 소스 복사 후 빌드
COPY . .
RUN ./gradlew :api-user:bootJar --no-daemon

# 2. 런타임 스테이지 (최소 환경)
FROM eclipse-temurin:${JAVA_VERSION}-jre-alpine
WORKDIR /app

# 보안: non-root user 생성
RUN addgroup -g 1000 spring && \
    adduser -u 1000 -G spring -s /bin/sh -D spring

# 빌드된 JAR 파일 복사
COPY --from=build /app/api-user/build/libs/*.jar app.jar

# 소유권 변경
RUN chown -R spring:spring /app
USER spring:spring

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
