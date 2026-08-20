# ============================================================
# Stage 1: Build
# ============================================================
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /build

# Copy Maven configuration first for dependency caching
COPY pom.xml .

RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build Eureka Server
RUN mvn clean package -DskipTests


# ============================================================
# Stage 2: Runtime
# ============================================================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Create non-root user
RUN useradd --system --create-home --shell /usr/sbin/nologin appuser

# Copy only the generated JAR
COPY --from=build /build/target/*.jar app.jar

# Eureka Server port
EXPOSE 8761

# Run as non-root user
USER appuser

# Start Eureka Server
ENTRYPOINT ["java", "-jar", "app.jar"]