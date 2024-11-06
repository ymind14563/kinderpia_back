# Gradle 빌드 이미지 설정 (Gradle 8.10.2와 JDK 17 포함)
FROM gradle:8.10.2-jdk17 AS build

# 작업 디렉토리 설정
WORKDIR /app

# 소스 코드 복사
COPY . .

# Gradle 빌드 실행 (테스트 제외: 이유 - properties 파일을 이미지에 포함시키지 않기 때문)
RUN gradle build -x test --no-daemon

# 실제 실행에 사용할 경량화된 JDK 이미지 설정
FROM openjdk:17-jdk-slim

# 빌드된 JAR 파일을 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 컨테이너에서 실행될 명령어 설정
CMD ["java", "-jar", "app.jar"]
