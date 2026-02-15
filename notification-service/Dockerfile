FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build
COPY pom.xml .
COPY common /build/common
COPY notification-service /build/notification-service
RUN mvn clean package -DskipTests -pl notification-service

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /build/notification-service/target/*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
