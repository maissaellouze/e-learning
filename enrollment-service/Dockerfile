FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build
COPY pom.xml .
COPY common /build/common
COPY enrollment-service /build/enrollment-service
RUN mvn clean package -DskipTests -pl enrollment-service

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /build/enrollment-service/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
