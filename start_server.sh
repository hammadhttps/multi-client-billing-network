#!/bin/bash

# Multi-Client Billing Network - Server Startup Script

echo "=========================================="
echo "Multi-Client Billing Network System"
echo "Starting Server Component..."
echo "=========================================="

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed or not in PATH"
    echo "Please install Java 17 or higher"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt "17" ]; then
    echo "❌ Java 17 or higher is required"
    echo "Current version: $JAVA_VERSION"
    exit 1
fi

echo "✅ Java version check passed"

# Compile the project if needed
echo "🔨 Compiling project..."
./mvnw compile -q

if [ $? -ne 0 ]; then
    echo "❌ Compilation failed"
    exit 1
fi

echo "✅ Compilation successful"

# Start the server
echo "🚀 Starting server on port 5000..."
echo "📝 Server logs will appear below:"
echo "=========================================="

./mvnw exec:java -Dexec.mainClass="Server.main.Server" -q