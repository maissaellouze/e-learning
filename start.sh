#!/bin/bash

# Script de démarrage de la plateforme e-learning sans Docker

set -e

PROJECT_HOME="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$PROJECT_HOME"

echo "======================================"
echo "E-Learning Platform - Startup Script"
echo "======================================"
echo ""

# Couleurs pour les messages
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Vérifier Java
echo -e "${YELLOW}Checking Java installation...${NC}"
if ! command -v java &> /dev/null; then
    echo -e "${RED}Java not found. Please install Java 17+${NC}"
    exit 1
fi
JAVA_VERSION=$(java -version 2>&1 | grep 'version' | awk '{print $3}' | tr -d '"')
echo -e "${GREEN}Java version: $JAVA_VERSION${NC}"
echo ""

# Vérifier Maven
echo -e "${YELLOW}Checking Maven installation...${NC}"
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}Maven not found. Please install Maven${NC}"
    exit 1
fi
echo -e "${GREEN}Maven found${NC}"
echo ""

# Vérifier RabbitMQ
echo -e "${YELLOW}Checking RabbitMQ...${NC}"
if command -v rabbitmq-server &> /dev/null; then
    echo -e "${GREEN}RabbitMQ found${NC}"
    # Start RabbitMQ if not running
    if ! pgrep -x "rabbitmq-server" > /dev/null; then
        echo -e "${YELLOW}Starting RabbitMQ...${NC}"
        rabbitmq-server -detached
        sleep 3
        echo -e "${GREEN}RabbitMQ started${NC}"
    else
        echo -e "${GREEN}RabbitMQ is already running${NC}"
    fi
else
    echo -e "${YELLOW}RabbitMQ not installed. Skipping RabbitMQ startup.${NC}"
    echo -e "${YELLOW}Install RabbitMQ for async messaging support${NC}"
fi
echo ""

# Compiler le projet
echo -e "${YELLOW}Building the project...${NC}"
mvn clean package -DskipTests -q
echo -e "${GREEN}Build completed${NC}"
echo ""

# Créer des fichiers de log
mkdir -p logs

# Démarrer les services
echo -e "${YELLOW}Starting services...${NC}"
echo ""

# Eureka Server
echo -e "${YELLOW}Starting Eureka Server (port 8761)...${NC}"
nohup java -jar eureka-server/target/eureka-server-1.0.0.jar > logs/eureka-server.log 2>&1 &
EUREKA_PID=$!
echo -e "${GREEN}Eureka Server started (PID: $EUREKA_PID)${NC}"
sleep 3
echo ""

# Auth Service
echo -e "${YELLOW}Starting Auth Service (port 8081)...${NC}"
nohup java -jar auth-service/target/auth-service-1.0.0.jar > logs/auth-service.log 2>&1 &
AUTH_PID=$!
echo -e "${GREEN}Auth Service started (PID: $AUTH_PID)${NC}"
sleep 3
echo ""

# Course Service
echo -e "${YELLOW}Starting Course Service (port 8082)...${NC}"
nohup java -jar course-service/target/course-service-1.0.0.jar > logs/course-service.log 2>&1 &
COURSE_PID=$!
echo -e "${GREEN}Course Service started (PID: $COURSE_PID)${NC}"
sleep 3
echo ""

# Enrollment Service
echo -e "${YELLOW}Starting Enrollment Service (port 8083)...${NC}"
nohup java -jar enrollment-service/target/enrollment-service-1.0.0.jar > logs/enrollment-service.log 2>&1 &
ENROLLMENT_PID=$!
echo -e "${GREEN}Enrollment Service started (PID: $ENROLLMENT_PID)${NC}"
sleep 3
echo ""

# Notification Service
echo -e "${YELLOW}Starting Notification Service (port 8084)...${NC}"
nohup java -jar notification-service/target/notification-service-1.0.0.jar > logs/notification-service.log 2>&1 &
NOTIFICATION_PID=$!
echo -e "${GREEN}Notification Service started (PID: $NOTIFICATION_PID)${NC}"
sleep 3
echo ""

# API Gateway
echo -e "${YELLOW}Starting API Gateway (port 8080)...${NC}"
nohup java -jar api-gateway/target/api-gateway-1.0.0.jar > logs/api-gateway.log 2>&1 &
GATEWAY_PID=$!
echo -e "${GREEN}API Gateway started (PID: $GATEWAY_PID)${NC}"
sleep 3
echo ""

# Save PIDs to file for later shutdown
cat > ".service-pids" << EOF
EUREKA_PID=$EUREKA_PID
AUTH_PID=$AUTH_PID
COURSE_PID=$COURSE_PID
ENROLLMENT_PID=$ENROLLMENT_PID
NOTIFICATION_PID=$NOTIFICATION_PID
GATEWAY_PID=$GATEWAY_PID
EOF

echo -e "${GREEN}======================================"
echo "All services started successfully!"
echo "=====================================${NC}"
echo ""
echo "Service URLs:"
echo "  - Eureka Server: http://localhost:8761"
echo "  - API Gateway: http://localhost:8080"
echo "  - Auth Service: http://localhost:8081"
echo "  - Course Service: http://localhost:8082"
echo "  - Enrollment Service: http://localhost:8083"
echo "  - Notification Service: http://localhost:8084"
echo ""
echo "View logs in: logs/"
echo ""
echo -e "${YELLOW}To stop all services, run: ./stop.sh${NC}"
