# Runtime image
FROM eclipse-temurin:21-jre

WORKDIR /app

# Create non-root user
RUN useradd --system --create-home --shell /usr/sbin/nologin appuser

# Copy application JAR
COPY target/*.jar app.jar

# Application port
EXPOSE 8082

# Don't run as root
USER appuser

# Start Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]