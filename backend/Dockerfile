# Stage 1: Build
FROM gradle:8.5.0-jdk21-jammy AS build
WORKDIR /app

# Copy source code with correct ownership
COPY --chown=gradle:gradle . .

# Build the application
# Skip tests to speed up build
RUN gradle bootJar -x test --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Create a non-privileged user
RUN useradd -m cyrcetech
USER cyrcetech

# Copy the jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
