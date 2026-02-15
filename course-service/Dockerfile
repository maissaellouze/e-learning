FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build
COPY pom.xml .
COPY common /build/common
COPY course-service /build/course-service
RUN mvn clean package -DskipTests -pl course-service

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /build/course-service/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
