# Nori Sales

Nori Sales is the sales and order management service of the Nori portfolio project.

The service is responsible for managing users, product categories, products, orders, payments and the communication between the sales and inventory domains.

This project was created as a practical backend application, with the goal of applying concepts commonly used in production systems, such as authentication, authorization, database migrations, asynchronous messaging and third-party payment integration.

## Project overview

Nori is composed of independent applications that work together:

* **nori-sales** — sales, orders, users and payments
* **nori-stock** — inventory and stock operations
* **nori-web** — web application used by buyers and administrators

The services have separate responsibilities and databases. Communication between sales and inventory is handled asynchronously through Kafka events.

This repository contains only the sales service.

## Main responsibilities

Nori Sales currently handles:

* user registration and authentication
* role-based access
* product category management
* product management
* order creation and consultation
* Pix payment generation through Mercado Pago
* payment confirmation through webhooks
* publication and consumption of Kafka events
* synchronization of product availability with the inventory service

## Architecture

The application is organized by business feature.

Each feature contains the classes related to its own responsibility, such as controllers, services, repositories, models, DTOs and mappers.

A simplified view of the project structure is:

```text
src/main/java/com/noriservices/norisales
├── domain
│   ├── category
│   ├── order
│   ├── payment
│   ├── product
│   └── user
├── infra
│   ├── configuration
│   ├── exception
│   ├── kafka
│   └── security
└── NoriSalesApplication.java
```

The main application flow follows this structure:

```text
HTTP Request
    ↓
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

Controllers are responsible for the HTTP layer.

Services contain the application and business rules.

Repositories handle database access through Spring Data JPA.

DTOs define the data received and returned by the API, avoiding direct exposure of persistence entities.

MapStruct is used to convert between entities and DTOs.

## Order and payment flow

The main business flow starts when an authenticated buyer creates an order.

```text
Buyer
  ↓
Creates an order
  ↓
Nori Sales validates the products and quantities
  ↓
The order is stored with a pending payment status
  ↓
A Pix payment is generated through Mercado Pago
  ↓
Mercado Pago confirms the payment through a webhook
  ↓
The order status is updated
  ↓
An event is published to Kafka
  ↓
Nori Stock processes the inventory operation
```

This approach keeps the payment and inventory processes separated from the original HTTP request.

It also reduces direct coupling between the sales and stock services.

## Communication with Nori Stock

Nori Sales and Nori Stock communicate through Kafka.

The sales service publishes events when an operation requires an inventory update. The stock service processes the event and performs the corresponding stock operation.

Nori Sales can also consume inventory events to keep the available quantity displayed in the sales application up to date.

This asynchronous communication allows each service to evolve independently and avoids a direct dependency between their internal implementations.

## Authentication and security

The API uses JWT-based authentication with Spring Security.

After a successful login, the client receives a token that must be sent in the `Authorization` header:

```http
Authorization: Bearer <token>
```

The application is stateless, so authentication information is not stored in an HTTP session.

Passwords are encrypted with BCrypt.

Public endpoints are limited to authentication operations and the Mercado Pago webhook. The remaining endpoints require a valid token.

The project supports different user roles, such as:

* `BUYER`
* `ADMIN`

Buyers can access their purchasing flow, while administrative operations are restricted according to the configured authorization rules.

## Technologies

* Java 21
* Spring Boot
* Spring Web MVC
* Spring Data JPA
* Spring Security
* JWT
* PostgreSQL
* Flyway
* Apache Kafka
* MapStruct
* Bean Validation
* Mercado Pago Java SDK
* Springdoc OpenAPI
* Maven

## Database migrations

Database changes are managed with Flyway.

Migration files are stored in:

```text
src/main/resources/db/migration
```

Flyway runs the pending migrations when the application starts.

The database structure should be changed through new migration files instead of manual updates.

## API documentation

The project uses Springdoc OpenAPI to generate interactive API documentation.

After starting the application, the Swagger interface should be available at:

```text
http://localhost:8080/nori-sales/v1/swagger-ui/index.html
```

The exact address may change according to the configured server port and context path.

## Requirements

Before running the project, make sure the following tools are available:

* Java 21
* Maven
* PostgreSQL
* Apache Kafka

A Mercado Pago test account and credentials are also required to use the payment flow.

## Environment variables

The application expects the following environment variables:

```env
BD_USERNAME=
BD_PASSWORD=

JWT_SECRET=
JWT_EXPIRATION=

MERCADOPAGO_ACCESS_TOKEN=

KAFKA_BOOTSTRAP_SERVERS=
```

Example values for local development:

```env
BD_USERNAME=postgres
BD_PASSWORD=postgres

JWT_SECRET=replace-with-a-secure-secret
JWT_EXPIRATION=86400000

MERCADOPAGO_ACCESS_TOKEN=your-mercado-pago-test-token

KAFKA_BOOTSTRAP_SERVERS=localhost:9092
```

Do not commit real credentials or production secrets to the repository.

## Local database

By default, the application connects to:

```text
jdbc:postgresql://localhost:5432/nori_sales
```

Create the database before starting the service:

```sql
CREATE DATABASE nori_sales;
```

The tables and database changes will be handled by Flyway migrations.

## Running the application

Clone the repository:

```bash
git clone https://github.com/jefflennon1/nori-sales.git
cd nori-sales
```

Configure the required environment variables and start the application:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

The API will be available under the configured context path:

```text
http://localhost:8080/nori-sales/v1
```

## Running the tests

Run the test suite with:

```bash
./mvnw test
```

On Windows:

```bash
mvnw.cmd test
```

## Main API resources

The API is organized around the following resources:

```text
/auth
/categories
/products
/orders
/payments
```

Examples:

```http
POST   /auth/login
GET    /categories
POST   /categories
GET    /products
POST   /products
GET    /orders
POST   /orders
POST   /payments/{orderId}/pix
POST   /payments/webhook
```

Some endpoints may require an administrative role.

The complete and current list of operations is available through Swagger.

## HTTP response conventions

The API follows standard HTTP status codes:

* `200 OK` for successful queries and updates
* `201 Created` when a resource is created
* `204 No Content` when an operation succeeds without a response body
* `400 Bad Request` for invalid input
* `401 Unauthorized` when authentication is missing or invalid
* `403 Forbidden` when the authenticated user does not have permission
* `404 Not Found` when a resource does not exist
* `409 Conflict` when an operation conflicts with the current state

Known application errors are handled centrally to keep error responses consistent.

## Development workflow

The project uses feature branches and pull requests for new changes.

A typical workflow is:

```bash
git checkout main
git pull

git checkout -b feature/order-tests
```

After finishing the change:

```bash
git add .
git commit -m "test(order): add order creation tests"
git push -u origin feature/order-tests
```

The branch can then be reviewed and merged through a pull request.

Suggested branch prefixes:

```text
feature/
fix/
refactor/
test/
docs/
chore/
```

Suggested commit examples:

```text
feat(order): add order cancellation
fix(payment): prevent duplicate payment confirmation
refactor(category): move validation to service layer
test(security): add authorization tests
docs(readme): document local environment setup
```

## Current project status

The main application flows are implemented, but the project is still evolving.

The current improvement work includes:

* increasing automated test coverage
* standardizing REST endpoints
* strengthening role-based authorization
* separating local and production configurations
* improving error responses
* adding observability and health checks
* improving the local development environment with containers
* documenting Kafka events and payloads

These improvements are being made incrementally through branches and pull requests.

## Related repositories

Nori is split into separate repositories:

* `nori-sales` — sales and payment service
* `nori-stock` — inventory service
* Nori web application — frontend for buyers and administrators

Links to the other repositories will be added as the project documentation is completed.

## Author

Developed by Jefferson Lennon as part of a portfolio focused on Java, Spring Boot, distributed systems and full-stack application development.
