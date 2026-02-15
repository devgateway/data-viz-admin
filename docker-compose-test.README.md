# Docker Compose Test Setup

This docker-compose file provides a local testing environment for the TCDI Admin application with all required dependencies.

## Services

| Service | Port | Description |
|---------|------|-------------|
| postgres | 5432 | PostgreSQL database for the admin application |
| postgres-interference | 5433 | PostgreSQL database for the interference service |
| eureka | 8761 | Netflix Eureka service registry |
| mock-interference-service | 8090 | MockServer simulating the interference service API |

## Prerequisites

- Docker and Docker Compose installed
- The admin application built (`mvn install -Dcheckstyle.skip=true -DskipTests`)

## Quick Start

1. **Start the infrastructure:**
   ```bash
   docker-compose -f docker-compose-test.yml up -d
   ```

2. **Wait for services to be healthy:**
   ```bash
   docker-compose -f docker-compose-test.yml ps
   ```

3. **Run the admin application:**
   ```bash
   mvn spring-boot:run -pl forms -Dcheckstyle.skip=true \
     -Dspring-boot.run.profiles=dev \
     -Dspring-boot.run.arguments="--eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/"
   ```

4. **Access the application:**
   - Admin UI: http://localhost:8080
   - Eureka Dashboard: http://localhost:8761

## Testing CSV Dataset Upload

### Scenario: Test normal upload flow

1. Navigate to the CSV Datasets section in the admin UI
2. Create a new dataset for the "INTERFERENCE" service
3. Upload the test CSV file: `forms/src/test/resources/world_industry_interference_2025.csv`
4. Click "Save and Publish"
5. The mock service will return a successful response

### Scenario: Test stuck publishing detection

1. Create and publish a dataset
2. The job checker runs every minute (`@Scheduled(cron = "0 * * * * *")`)
3. If a job is in PUBLISHING state for longer than `dataset.publishing.timeout.minutes` (default: 30), it will be marked as ERROR_IN_PUBLISHING

### Scenario: Test Cancel Publishing button

1. Create a dataset and start publishing
2. While in PUBLISHING state, a "Cancel Publishing" button should appear
3. Click the button to cancel and mark as ERROR_IN_PUBLISHING
4. This allows retrying the upload after fixing issues

## Configuration

### Publishing Timeout

Configure the timeout for stuck jobs in `application.properties` or via environment variable:

```properties
dataset.publishing.timeout.minutes=30
```

### Mock Service Responses

The mock interference service (MockServer) is configured via `mockserver-init.json`. It simulates:

- `GET /health` - Health check endpoint
- `POST /datasets` - Dataset upload (returns PROCESSING status)
- `GET /jobs/code/tcdi-*` - Job status check (returns COMPLETED)
- `DELETE /datasets/tcdi-*` - Dataset deletion
- `GET /template/download` - CSV template download
- `GET /dimensions` - Get available dimensions
- `GET /measures` - Get available measures

## Cleanup

Stop and remove all containers and volumes:

```bash
docker-compose -f docker-compose-test.yml down -v
```

## Troubleshooting

### Database connection issues

Check if PostgreSQL is healthy:
```bash
docker-compose -f docker-compose-test.yml logs postgres
```

### Eureka connection issues

Ensure Eureka is running and accessible:
```bash
curl http://localhost:8761/actuator/health
```

### Mock service not responding

Check MockServer logs:
```bash
docker-compose -f docker-compose-test.yml logs mock-interference-service
```

## Running Unit Tests

The `DatasetClientServiceTest` class contains unit tests for the new functionality:

```bash
mvn test -pl forms -Dtest=DatasetClientServiceTest -Dcheckstyle.skip=true
```

Tests cover:
- Cancel publishing functionality
- Status transitions (PUBLISHING → PUBLISHED, UNPUBLISHING → DRAFT)
- Error status transitions (PUBLISHING → ERROR_IN_PUBLISHING, UNPUBLISHING → ERROR_IN_UNPUBLISHING)
- Timeout detection for stuck jobs
- Dataset saving based on type (CSV vs Tetsim)
