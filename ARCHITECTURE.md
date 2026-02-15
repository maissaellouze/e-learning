# Architecture Overview

## System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         Client Requests                          │
└────────────────┬────────────────────────────────────────────────┘
                 │
                 ▼
         ┌──────────────────┐
         │   API Gateway    │
         │   (Port 8080)    │
         │  Spring Cloud    │
         │     Gateway      │
         └────────┬─────────┘
                  │
      ┌───────────┼───────────────────────────┐
      │           │                           │
      ▼           ▼                           ▼
 ┌──────────┐ ┌──────────┐           ┌──────────────┐
 │  Eureka  │ │  Service │◄──────────┤ Service      │
 │  Server  │ │Discovery │◄──────────┤ Registration │
 │(8761)    │ │ Queries  │           │ (on startup) │
 └──────────┘ └──────────┘           └──────────────┘
      │
      │ Service Registry
      │ (service names & locations)
      │
      ├──────────────┬──────────────┬──────────────┬─────────────┐
      │              │              │              │             │
      ▼              ▼              ▼              ▼             ▼
  ┌────────┐    ┌────────┐    ┌────────┐    ┌────────┐    ┌──────────┐
  │  Auth  │    │ Course │    │Enroll- │    │Notifi- │    │ Additional
  │Service │    │Service │    │ment    │    │cation  │    │ Services
  │(8081)  │    │(8082)  │    │Service │    │Service │    │(auto-
  │        │    │        │    │(8083)  │    │(8084)  │    │discovered)
  └────────┘    └────────┘    └────────┘    └────────┘    └──────────┘
      │              │              │              │             │
      └──────────────┴──────────────┴──────────────┴─────────────┘
                              │
                    ┌─────────┴─────────┐
                    │                   │
            Business Logic        H2 In-Memory
                                Database
```

## Request Flow Example

```
1. Client makes request:
   GET http://localhost:8080/api/courses

2. API Gateway receives request:
   - Matches route: /api/courses/** → lb://course-service
   
3. Gateway queries Eureka:
   - Lookup: "course-service"
   - Gets: Instance at localhost:8082
   
4. Gateway forwards request:
   - GET http://localhost:8082/api/courses
   
5. Course Service processes:
   - Business logic execution
   - Database operations (H2)
   
6. Response returned:
   - Course Service → API Gateway → Client
```

## Service Registration Flow

```
┌──────────────────────────────────────┐
│  Microservice Startup Sequence       │
└──────────────────────────────────────┘
            │
            ▼
┌──────────────────────────────────────┐
│ 1. Spring Boot Application Starts    │
│    @EnableDiscoveryClient annotation │
└──────────────────────────────────────┘
            │
            ▼
┌──────────────────────────────────────┐
│ 2. Eureka Client Initializes         │
│    Reads configuration from          │
│    application.yml                   │
└──────────────────────────────────────┘
            │
            ▼
┌──────────────────────────────────────┐
│ 3. Service Registers with Eureka     │
│    - Service name: ${spring.app.name}│
│    - Host/Port: from configuration   │
│    - Health check: enabled           │
└──────────────────────────────────────┘
            │
            ▼
┌──────────────────────────────────────┐
│ 4. Eureka Server Updates Registry    │
│    - Service becomes discoverable    │
│    - Shows up in Eureka Dashboard    │
└──────────────────────────────────────┘
            │
            ▼
┌──────────────────────────────────────┐
│ 5. Service Ready for Requests        │
│    - API Gateway discovers it        │
│    - Load balancing enabled          │
│    - Health checks ongoing           │
└──────────────────────────────────────┘
```

## Dynamic Routing Example

```
API Gateway Configuration:
─────────────────────────

routes:
  - id: course-service
    uri: lb://course-service          ← Load-balanced
    predicates:
      - Path=/api/courses/**

Request: GET /api/courses/123
         │
         ├─ Matches predicate: /api/courses/**  ✓
         │
         ├─ Extract service name: "course-service"
         │
         ├─ Query Eureka for "course-service"
         │  └─ Response: Instance at localhost:8082
         │
         └─ Route to: http://localhost:8082/api/courses/123
```

## Load Balancing Scenario

```
If multiple instances of a service are running:

Eureka Registry:
┌─────────────────────┐
│  course-service     │
├─────────────────────┤
│ Instance 1: :8082   │
│ Instance 2: :8082   │ (different machine)
│ Instance 3: :8082   │ (different machine)
└─────────────────────┘

Request arrives at Gateway:
GET /api/courses/123

Load Balancing (Round-robin):
Request 1 → Instance 1
Request 2 → Instance 2
Request 3 → Instance 3
Request 4 → Instance 1 (round-robin restart)

Benefits:
- Distributes load evenly
- Automatic failover if instance down
- No manual configuration needed
```

## Eureka Dashboard Information

```
URL: http://localhost:8761/

Shows:
├── System Status
│   ├── Environment: Standalone
│   ├── Data center: Default
│   └── Current time (UTC)
│
├── Instances Currently Registered
│   ├── auth-service (1 instance)
│   ├── course-service (1-N instances)
│   ├── enrollment-service (1 instance)
│   └── notification-service (1 instance)
│
└── General Info
    ├── Total available memory
    ├── Environment properties
    └── Instance metadata
```

## Technology Stack

```
┌─────────────────────────────────────┐
│      Spring Boot 3.2.0              │
├─────────────────────────────────────┤
│      Spring Cloud 2023.0.3          │
├─────────────────────────────────────┤
│  ┌─ Spring Cloud Netflix Eureka     │
│  │  - Service Discovery             │
│  │  - Registry Server               │
│  │                                  │
│  ├─ Spring Cloud Gateway            │
│  │  - API Gateway                   │
│  │  - Dynamic Routing               │
│  │  - Load Balancing                │
│  │                                  │
│  └─ Spring Cloud Commons            │
│     - Service Discovery Client      │
│     - Load Balancer                 │
├─────────────────────────────────────┤
│  ┌─ Spring Data JPA                 │
│  ├─ Spring Security                 │
│  ├─ Spring WebFlux                  │
│  ├─ Spring RabbitMQ                 │
│  ├─ H2 Database                     │
│  └─ Actuator & Management           │
└─────────────────────────────────────┘
```

## Deployment Modes

### Mode 1: Standalone (Development)
```
Single machine, all services on localhost:
- Eureka:      localhost:8761
- Gateway:     localhost:8080
- Auth:        localhost:8081
- Course:      localhost:8082
- Enrollment:  localhost:8083
- Notification: localhost:8084
```

### Mode 2: Distributed (Production-Ready)
```
Services can run on different machines:
Machine 1:  Eureka Server (8761)
Machine 2:  API Gateway (8080)
Machine 3+: Microservices (any ports)

All services point to Eureka:
eureka.client.serviceUrl.defaultZone=http://eureka-machine:8761/eureka/
```

### Mode 3: Docker Containers
```
Each service in a container with bridge networking:
- Eureka Service: eureka-server:8761
- Gateway Service: api-gateway:8080
- Auth Service: auth-service:8081
- etc.

Containers discovered via Docker DNS
```

## Configuration Hierarchy

```
Parent POM (pom.xml)
    ↓
    ├─── Eureka Server Module
    │    ├─ Dependencies
    │    ├─ @EnableEurekaServer
    │    └─ application.yml
    │
    ├─── API Gateway Module
    │    ├─ Dependencies (Gateway + Eureka Client)
    │    ├─ @EnableDiscoveryClient
    │    ├─ application.yml (Routes + Discovery)
    │    └─ Load Balancer Config
    │
    └─── Microservices (Auth, Course, Enrollment, Notification)
         ├─ Dependencies (Eureka Client)
         ├─ @EnableDiscoveryClient
         ├─ application.yml (Service name + Port + Eureka)
         └─ Business Logic
```

