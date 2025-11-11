# ----------------------------------------------------------------------------------
# Stage 1: 빌더 (Builder) - JDK, Gradle을 사용하여 JAR 파일을 생성하는 단계
# ----------------------------------------------------------------------------------
# Gradle 빌드 환경이 포함된 JDK 이미지를 사용합니다.
FROM gradle:8.5.0-jdk17 AS builder 
WORKDIR /app

# 1. 의존성 캐싱 (선택 사항: 빌드 속도 향상)
# build.gradle, settings.gradle 파일을 복사하고 의존성을 다운로드하여 레이어 캐시를 활용
COPY build.gradle.kts settings.gradle.kts ./
RUN gradle dependencies --no-daemon

# 2. 소스 코드 복사 및 빌드
# 모든 소스 코드를 복사하고 테스트를 제외한 최종 JAR 파일을 생성합니다.
COPY . .
RUN gradle clean build -x test --no-daemon

# ----------------------------------------------------------------------------------
# Stage 2: 러너 (Runner) - JRE만 사용하여 JAR 파일을 실행하는 최종 이미지 단계
# ----------------------------------------------------------------------------------
# JRE만 포함된 경량 이미지를 사용하여 최종 이미지 크기를 최소화합니다.
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 파일 경로는 Spring Boot Gradle 플러그인의 기본 출력 경로를 가정합니다.
COPY --from=builder /app/build/libs/app.jar ./app.jar

# 컨테이너 시간대를 서울 시간으로 설정 (운영 환경 관리에 유리)
ENV TZ=Asia/Seoul

# 최종 실행 명령어
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
