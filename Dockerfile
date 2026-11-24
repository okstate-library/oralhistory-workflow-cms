
# ==================================================
# Stage 1: Build application using Maven and Java 21
# ==================================================
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy Maven configuration
COPY pom.xml .

# Download dependencies first for better build caching
RUN mvn -B dependency:go-offline

# Copy application source
COPY src ./src

# Build executable Spring Boot JAR
RUN mvn -B clean package -DskipTests


# ==================================================
# Stage 2: Production runtime using Java 21
# ==================================================
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Create non-root application user
RUN groupadd --system spring \
    && useradd --system --gid spring spring

# Copy application JAR
COPY --from=builder --chown=spring:spring \
    /app/target/oralhistory-workflow-cms-2.3.0.jar \
    /app/app.jar

# Application port
EXPOSE 8080

# JVM configuration
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+UseG1GC"

# Run application as non-root user
USER spring

# Start Spring Boot application
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
