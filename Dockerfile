FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# [중요] 프로젝트의 모든 파일을 도커 안으로 복사
COPY . .

RUN chmod +x ./mvnw

RUN ./mvnw clean package -DskipTests





FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]