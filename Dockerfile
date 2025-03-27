FROM maven:3-amazoncorretto-21 AS base

FROM base AS builder
WORKDIR /app

COPY . /app
# Use cache mounts so dependencies aren't re-downloaded on every build
RUN --mount=type=cache,target=/root/.m2 mvn -B clean install -DskipTests -Dcheckstyle.skip

# Create directory for extracting the JAR
RUN mkdir -p forms/target/deps

# Extract the JAR contents
FROM builder AS assembler
WORKDIR /app/forms/target/deps
RUN jar -xf ../*.jar

# Build the runtime image
FROM openjdk:21-jdk-slim AS runtime
WORKDIR /opt/devgateway/tcdi/admin

# Copy the application code
COPY --from=assembler /app/forms/target/deps ./deps

# Copy entrypoint script
COPY entrypoint.sh ./
EXPOSE 8080
RUN chmod +x entrypoint.sh
ENTRYPOINT ["/opt/devgateway/tcdi/admin/entrypoint.sh"]
CMD ["admin"]