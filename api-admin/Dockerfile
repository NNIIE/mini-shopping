# 1. Java 21이 들어있는 공식 경량 이미지를 사용
FROM eclipse-temurin:21-jdk-alpine

# 2. 빌드 산출물(jar) 복사
COPY api-admin/build/libs/*.jar app.jar

# 3. 컨테이너가 실행되면 jar 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
