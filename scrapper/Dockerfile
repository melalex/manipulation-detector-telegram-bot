# ----------- Stage 1: Build ----------
FROM hseeberger/scala-sbt:17.0.2_1.6.2_2.13.8 AS builder

WORKDIR /app

# Copy only build files first to leverage Docker cache
COPY build.sbt .
COPY project ./project

# Download dependencies (faster rebuilds)
RUN sbt update

# Now copy the rest of the source code
COPY . .

# Compile and package the app
RUN sbt assembly

# ----------- Stage 2: Runtime ----------
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy only the final fat JAR from the builder stage
COPY --from=builder /app/target/scala-2.13/*.jar app.jar

# Run the bot
ENTRYPOINT ["java", "-jar", "app.jar"]
    