#!/bin/bash

# Quick start script for E-Learning Platform with Eureka & API Gateway
# This script starts all services in order

echo "=========================================="
echo "E-Learning Platform - Service Startup"
echo "=========================================="
echo ""

BASE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to start a service
start_service() {
    local service_name=$1
    local port=$2
    local jar_file=$3
    
    echo -e "${YELLOW}Starting $service_name (Port $port)...${NC}"
    
    if [ ! -f "$jar_file" ]; then
        echo -e "${RED}ERROR: $jar_file not found. Please run 'mvn clean install -DskipTests=true' first${NC}"
        return 1
    fi
    
    java -jar "$jar_file" &
    SERVICE_PID=$!
    echo -e "${GREEN}✓ $service_name started (PID: $SERVICE_PID)${NC}"
    echo "$SERVICE_PID" > "$BASE_DIR/.${service_name}.pid"
    echo ""
    sleep 2
}

# Check if all JARs exist
echo "Checking if all services are built..."
SERVICES=(
    "eureka-server:8761:eureka-server/target/eureka-server-1.0.0.jar"
    "api-gateway:8080:api-gateway/target/api-gateway-1.0.0.jar"
    "auth-service:8081:auth-service/target/auth-service-1.0.0.jar"
    "course-service:8082:course-service/target/course-service-1.0.0.jar"
    "enrollment-service:8083:enrollment-service/target/enrollment-service-1.0.0.jar"
    "notification-service:8084:notification-service/target/notification-service-1.0.0.jar"
)

MISSING=false
for service in "${SERVICES[@]}"; do
    IFS=':' read -r name port jar <<< "$service"
    if [ ! -f "$BASE_DIR/$jar" ]; then
        echo -e "${RED}✗ Missing: $name ($jar)${NC}"
        MISSING=true
    fi
done

if [ "$MISSING" = true ]; then
    echo ""
    echo -e "${YELLOW}Building all modules...${NC}"
    cd "$BASE_DIR"
    mvn clean install -DskipTests=true
    if [ $? -ne 0 ]; then
        echo -e "${RED}Build failed!${NC}"
        exit 1
    fi
fi

echo -e "${GREEN}All services are built!${NC}"
echo ""

# Start services in order
echo "Starting services in order..."
echo ""

start_service "eureka-server" "8761" "$BASE_DIR/eureka-server/target/eureka-server-1.0.0.jar"
sleep 3

start_service "api-gateway" "8080" "$BASE_DIR/api-gateway/target/api-gateway-1.0.0.jar"
sleep 2

start_service "auth-service" "8081" "$BASE_DIR/auth-service/target/auth-service-1.0.0.jar"
start_service "course-service" "8082" "$BASE_DIR/course-service/target/course-service-1.0.0.jar"
start_service "enrollment-service" "8083" "$BASE_DIR/enrollment-service/target/enrollment-service-1.0.0.jar"
start_service "notification-service" "8084" "$BASE_DIR/notification-service/target/notification-service-1.0.0.jar"

echo ""
echo "=========================================="
echo -e "${GREEN}All services started successfully!${NC}"
echo "=========================================="
echo ""
echo "Service URLs:"
echo "  Eureka Dashboard:      http://localhost:8761"
echo "  API Gateway:           http://localhost:8080"
echo "  Auth Service (direct): http://localhost:8081"
echo "  Course Service (direct): http://localhost:8082"
echo "  Enrollment Service (direct): http://localhost:8083"
echo "  Notification Service (direct): http://localhost:8084"
echo ""
echo "Example API Calls (via API Gateway):"
echo "  curl http://localhost:8080/auth/login"
echo "  curl http://localhost:8080/api/courses"
echo "  curl http://localhost:8080/api/enrollments"
echo "  curl http://localhost:8080/api/notifications"
echo ""
echo "To stop all services, run: ./stop.sh"
echo ""

# Show running services
echo "Running services:"
ps aux | grep java | grep -E '(eureka|gateway|auth|course|enrollment|notification)' | grep -v grep | awk '{print "  PID " $2 ": " $NF}'

