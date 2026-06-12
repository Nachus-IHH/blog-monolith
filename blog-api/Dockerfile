# Dockerfile

# === 1. Compilacion ===
FROM eclipse-temurin:17-jdk-alpine-3.21 AS builder

WORKDIR /app

# Copy wrapper from maven & file of config POM
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies for save in Docker cache
RUN ./mvnw dependency:go-offline -B

# Copy source code & compile JAR skipping test for now
COPY src ./src
RUN ./mvnw clean package -DskipTests



# === 2. Ejecucion ===
FROM eclipse-temurin:17-jre-alpine-3.21

# Create an user no root
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

# Copy the compiled JAR file from the previous stage
COPY --from=builder --chown=spring:spring /app/target/blog-api.jar app.jar

# Use user no root
USER spring

# Spring Boot port exposed
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
