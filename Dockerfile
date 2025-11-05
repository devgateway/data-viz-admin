# Stage 1: Download dependencies (cached separately from source code)
FROM maven:3-eclipse-temurin-21 AS dependencies
WORKDIR /app

# Copy only POM files first to cache dependency downloads
COPY pom.xml ./
COPY persistence/pom.xml ./persistence/
COPY web/pom.xml ./web/
COPY forms/pom.xml ./forms/

# Download all dependencies (this layer will be cached unless POM files change)
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B dependency:go-offline -DskipTests -Dcheckstyle.skip || true

# Stage 2: Build the application
FROM dependencies AS builder
WORKDIR /app

# Accept git branch/tag as build argument
ARG GIT_BRANCH=unknown

# Now copy source code (changes here won't invalidate dependency cache)
COPY persistence/src ./persistence/src
COPY web/src ./web/src
COPY forms/src ./forms/src
COPY checkstyle.xml checkstyle-suppressions.xml ./

# Build with cache mount for any additional dependencies
# Pass git.closest.tag.name as a Maven property
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B clean package -DskipTests -Dcheckstyle.skip -Dgit.closest.tag.name=${GIT_BRANCH}

# Stage 3: Extract JAR layers (Spring Boot layered JAR)
FROM builder AS assembler
WORKDIR /app/forms/target/deps
RUN jar -xf ../tcdi-admin-forms-*.jar

# Stage 4: Build the runtime image
FROM eclipse-temurin:21-jre-alpine AS runtime
RUN apk add --no-cache bash curl
WORKDIR /opt/devgateway/tcdi/admin

# Copy the extracted application in optimal order for layer caching
COPY --from=assembler /app/forms/target/deps ./deps

# Copy entrypoint script and set permissions in one layer
COPY entrypoint.sh ./
RUN chmod +x entrypoint.sh

EXPOSE 8080
ENTRYPOINT ["/opt/devgateway/tcdi/admin/entrypoint.sh"]
CMD ["admin"]