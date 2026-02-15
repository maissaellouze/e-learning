#!/bin/bash

# Script pour arrêter tous les services

set -e

echo "Stopping all services..."
echo ""

# Load PIDs
if [ -f ".service-pids" ]; then
    source .service-pids
    
    # Kill processes
    for pid in $EUREKA_PID $AUTH_PID $COURSE_PID $ENROLLMENT_PID $NOTIFICATION_PID $GATEWAY_PID; do
        if [ ! -z "$pid" ] && kill -0 "$pid" 2>/dev/null; then
            echo "Stopping service with PID $pid..."
            kill $pid
        fi
    done
    
    rm -f ".service-pids"
    echo "All services stopped"
else
    echo "No service PIDs found. Services might not be running."
    echo "Killing by port..."
    
    # Alternative: kill by port using lsof
    for port in 8080 8081 8082 8083 8084 8761; do
        pid=$(lsof -t -i :$port 2>/dev/null || true)
        if [ ! -z "$pid" ]; then
            echo "Killing process on port $port (PID: $pid)"
            kill $pid 2>/dev/null || true
        fi
    done
fi

echo ""
echo "Services stopped successfully"
