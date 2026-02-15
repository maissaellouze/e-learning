FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build
COPY pom.xml .
COPY common /build/common
COPY eureka-server /build/eureka-server
RUN mvn clean package -DskipTests -pl eureka-server

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /build/eureka-server/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
