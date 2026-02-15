# API Gateway & Eureka Service Discovery Setup

## Architecture Overview

This document describes the implementation of:
- **Eureka Server**: Service discovery and registration
- **API Gateway**: Centralized routing with dynamic service discovery and load balancing
- **Microservices**: Auto-registration with Eureka for dynamic routing

## Components

### 1. Eureka Server (Port 8761)
- Service registry for automatic service discovery
- Dashboard available at `http://localhost:8761/`
- Location: `eureka-server` module

### 2. API Gateway (Port 8080)
- Centralized entry point for all requests
- Uses Spring Cloud Gateway for routing
- Dynamically routes to services via Eureka service discovery
- Load balancing support with `lb://` prefix

### 3. Microservices (Auto-discoverable)
- **Auth Service** (Port 8081): Authentication & authorization
- **Course Service** (Port 8082): Course management
- **Enrollment Service** (Port 8083): Student enrollment
- **Notification Service** (Port 8084): Notifications

All services automatically register with Eureka on startup.

## Project Structure

```
elearning-platform/
├── eureka-server/              # Service Discovery Server
├── api-gateway/                # API Gateway Router
├── auth-service/               # Microservice 1
├── course-service/             # Microservice 2
├── enrollment-service/         # Microservice 3
├── notification-service/       # Microservice 4
├── common/                     # Shared utilities
└── docker-compose.yml          # Optional: Docker setup
```

## Key Dependencies

### Parent POM (Updated to Spring Cloud 2023.0.3)
```xml
<spring-cloud.version>2023.0.3</spring-cloud.version>
<dependencyManagement>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-dependencies</artifactId>
        <version>2023.0.3</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
</dependencyManagement>
```

### Eureka Server Dependencies
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

### Microservices Dependencies
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

### API Gateway Dependencies
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

## Application Configuration

### Eureka Server (application.yml)
```yaml
server:
  port: 8761

eureka:
  instance:
    hostname: localhost
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
  server:
    enableSelfPreservation: false
    evictionIntervalTimerInMs: 1000

management:
  endpoints:
    web:
      exposure:
        include: "*"
```

### Microservices (application.yml)
Each microservice includes Eureka client configuration:
```yaml
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
  instance:
    preferIpAddress: false
```

### API Gateway (application.yml)
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: auth-service
          uri: lb://auth-service           # Load-balanced service discovery
          predicates:
            - Path=/auth/**
        - id: course-service
          uri: lb://course-service
          predicates:
            - Path=/api/courses/**
        - id: enrollment-service
          uri: lb://enrollment-service
          predicates:
            - Path=/api/enrollments/**
        - id: notification-service
          uri: lb://notification-service
          predicates:
            - Path=/api/notifications/**
      discovery:
        locator:
          enabled: true                    # Dynamic service discovery
          lowerCaseServiceId: true

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
  instance:
    preferIpAddress: false
```

## Starting the Services

### Option 1: Individual JAR Files (Recommended for Development)

#### Terminal 1 - Start Eureka Server
```bash
cd elearning-platform/eureka-server
java -jar target/eureka-server-1.0.0.jar
```

#### Terminal 2 - Start API Gateway
```bash
cd elearning-platform/api-gateway
java -jar target/api-gateway-1.0.0.jar
```

#### Terminal 3+ - Start Microservices (in any order)
```bash
# Auth Service
cd elearning-platform/auth-service
java -jar target/auth-service-1.0.0.jar

# Course Service
cd elearning-platform/course-service
java -jar target/course-service-1.0.0.jar

# Enrollment Service
cd elearning-platform/enrollment-service
java -jar target/enrollment-service-1.0.0.jar

# Notification Service
cd elearning-platform/notification-service
java -jar target/notification-service-1.0.0.jar
```

### Option 2: Maven Spring Boot Plugin
```bash
# From each service directory
mvn spring-boot:run
```

### Option 3: Docker Compose
```bash
# From project root
docker-compose up -d
```

## Accessing the Services

### Eureka Dashboard
- URL: `http://localhost:8761/`
- Shows all registered services and their status

### API Gateway (Entry Point)
- URL: `http://localhost:8080/`
- All requests go through this gateway

### Example Requests via API Gateway

#### Auth Service
```bash
curl http://localhost:8080/auth/login
```

#### Course Service
```bash
curl http://localhost:8080/api/courses
```

#### Enrollment Service
```bash
curl http://localhost:8080/api/enrollments
```

#### Notification Service
```bash
curl http://localhost:8080/api/notifications
```

### Direct Service Access (if needed)
- Auth Service: `http://localhost:8081`
- Course Service: `http://localhost:8082`
- Enrollment Service: `http://localhost:8083`
- Notification Service: `http://localhost:8084`

## Service Registration Flow

1. **Eureka Server Starts** (Port 8761)
   - Initializes the service registry
   - Dashboard becomes available

2. **API Gateway Starts** (Port 8080)
   - Registers itself with Eureka
   - Discovers all available services

3. **Microservices Start** (Ports 8081-8084)
   - Each service registers with Eureka
   - Becomes discoverable by other services and the gateway

4. **Request Routing**
   - Client request → API Gateway (Port 8080)
   - Gateway queries Eureka for service location
   - Gateway routes to discovered service instance
   - Load balancing happens automatically if multiple instances exist

## Dynamic Load Balancing

The API Gateway uses the `lb://` (load balanced) prefix for service URIs:

```yaml
uri: lb://service-name
```

This enables:
- **Service Discovery**: Looks up service by name in Eureka
- **Load Balancing**: Distributes requests across multiple instances
- **Failover**: Automatically routes to healthy instances
- **Dynamic Scaling**: New instances are automatically discovered

## Monitoring & Management

### Actuator Endpoints
Each service exposes actuator endpoints:
```
http://localhost:PORT/actuator
http://localhost:PORT/actuator/health
http://localhost:PORT/actuator/info
http://localhost:PORT/actuator/metrics
```

### Service Discovery Status
Check registered services:
```bash
curl http://localhost:8761/eureka/apps
```

## Troubleshooting

### Service Not Appearing in Eureka
1. Check service startup logs for errors
2. Verify Eureka server is running and accessible
3. Confirm `eureka.client.serviceUrl.defaultZone` is correct
4. Check network connectivity between services

### Gateway Routing Issues
1. Verify service name matches Eureka registration
2. Check gateway configuration for correct route predicates
3. Ensure service is registered in Eureka before accessing
4. Check gateway logs for routing errors

### Port Conflicts
If ports are already in use, update `server.port` in `application.yml` for conflicting services

## Configuration Summary

| Component | Port | Eureka | Status |
|-----------|------|--------|--------|
| Eureka Server | 8761 | N/A | Server |
| API Gateway | 8080 | ✓ Registered | Client |
| Auth Service | 8081 | ✓ Registered | Client |
| Course Service | 8082 | ✓ Registered | Client |
| Enrollment Service | 8083 | ✓ Registered | Client |
| Notification Service | 8084 | ✓ Registered | Client |

## Building the Project

### Build All Modules
```bash
mvn clean install -DskipTests=true
```

### Build Specific Module
```bash
mvn clean package -DskipTests=true -pl eureka-server
mvn clean package -DskipTests=true -pl api-gateway
```

## Next Steps

1. **Service Mesh** (Optional): Add Istio for advanced traffic management
2. **API Documentation**: Add Swagger/OpenAPI integration
3. **Circuit Breaker**: Implement Resilience4j or Hystrix
4. **Rate Limiting**: Add Spring Cloud Gateway rate limiting
5. **Security**: Implement OAuth2/JWT authentication
6. **Monitoring**: Add Prometheus & Grafana for metrics

