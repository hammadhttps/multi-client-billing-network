#!/bin/bash

# Multi-Client Billing Network - Client Startup Script

echo "=========================================="
echo "Multi-Client Billing Network System"
echo "Starting Client Application..."
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

# Check if server is running
echo "🔍 Checking server connection..."
if ! nc -z localhost 5000 2>/dev/null; then
    echo "⚠️  Warning: Server might not be running on port 5000"
    echo "💡 Make sure to start the server first using: ./start_server.sh"
    echo ""
    echo "Attempting to start client anyway..."
fi

# Start the client
echo "🚀 Starting JavaFX client application..."
echo "📝 Application logs will appear below:"
echo "=========================================="

./mvnw javafx:run -q