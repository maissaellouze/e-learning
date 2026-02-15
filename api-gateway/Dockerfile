FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build
COPY pom.xml .
COPY common /build/common
COPY api-gateway /build/api-gateway
RUN mvn clean package -DskipTests -pl api-gateway

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /build/api-gateway/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
