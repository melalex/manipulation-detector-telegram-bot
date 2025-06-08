# === STAGE 1: Build the app with sbt ===
FROM hseeberger/scala-sbt:17.0.2_1.6.2_2.13.8 AS builder

WORKDIR /app

# Copy build files
COPY build.sbt ./
COPY project ./project

# Cache dependencies
RUN sbt update

# Copy source code
COPY src ./src

ENV SBT_OPTS="-Xmx2G -Xss2M -XX:MaxMetaspaceSize=512M"

# Compile and package the app
RUN sbt package

# === STAGE 2: Run the app with Spark ===
FROM bitnami/spark:4.0.0

WORKDIR /opt/spark-app

# Copy JAR from build stage
COPY --from=builder /app/target/scala-2.13/manipulation-detector-telegram-pipeline_2.13-0.1.0-SNAPSHOT.jar ./manipulation-detector-telegram-pipeline.jar

# Set default command (can be overridden in docker-compose)
CMD ["spark-submit", "--class", "com.melalex.detector.TelegramNERPipeline", "./manipulation-detector-telegram-pipeline.jar"]
