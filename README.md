# Clinic Management System

A RESTful backend for managing a clinic's **patients, doctors, appointments and prescriptions**, built with **Java and Spring Boot**. It follows a layered architecture and is secured with **JWT authentication**.

> Built during my backend microservices apprenticeship at Acabes (Arab Bank's fintech innovation arm).

<!-- Items in [square brackets] are placeholders. Replace them with your real values, then delete this comment. -->

---

## Features

- **Patient management** – create, view, update and delete patient records
- **Doctor management** – maintain doctor profiles
- **Appointment management** – book appointments between patients and doctors, with **filtering** of appointments
- **Prescription management** – issue and track prescriptions linked to appointments/patients
- **JWT security** – stateless authentication using a custom `JwtAuthenticationFilter`, `JwtUtil` and `SecurityConfig`
- **Caching** with Spring's `@Cacheable` / `@CachePut`
- **DTO layer** so API responses don't expose entities directly
- **H2 in-memory database** for quick prototyping and testing
- API testing with **Postman** [and documentation with Swagger UI, if enabled in this project]

---

## Tech stack

| Area | Technology |
|---|---|
| Language | Java [17 / 21] |
| Framework | Spring Boot [version] |
| Security | Spring Security + JWT |
| Persistence | Spring Data JPA, H2 (in-memory) [or PostgreSQL / MySQL] |
| Build tool | Maven |
| Testing | Postman [+ JUnit, if you wrote tests] |

---

## Architecture

The project uses a classic layered design:

```
Controller  →  Service  →  Repository  →  Database
     ↑             ↑
    DTO        Entity / Model
```

| Layer | Responsibility |
|---|---|
| **Model / Entity** | JPA entities: `Patient`, `Doctor`, `Appointment`, `Prescription` |
| **Repository** | Spring Data JPA interfaces for database access |
| **Service** | Business logic (service interfaces + implementations) |
| **Controller** | REST endpoints (`@RestController`) |
| **DTO** | Request/response objects between client and API |
| **Security** | `JwtUtil`, `JwtAuthenticationFilter`, `SecurityConfig` |

### Entity relationships

- A **Patient** can have many **Appointments**
- A **Doctor** can have many **Appointments**
- An **Appointment** can have **Prescriptions** [adjust if prescriptions link to the patient/doctor directly]

---

## Getting started

### Prerequisites

- JDK [17+]
- Maven 3.8+
- Postman (optional, for testing)

### Run locally

```bash
git clone https://github.com/<your-username>/<repo-name>.git
cd <repo-name>
mvn clean install
mvn spring-boot:run
```

The API starts at `http://localhost:8080` [change if you use a different port].

### Database

By default the app uses an **H2 in-memory database**, so no setup is needed. Data is reset every time the app restarts.

To use the H2 console (if enabled), open `http://localhost:8080/h2-console` and use the JDBC URL from `application.properties`.

To switch to PostgreSQL or MySQL, update the datasource settings in `src/main/resources/application.properties`.

---

## Authentication

The API uses **JWT (JSON Web Tokens)**:

1. Authenticate with your credentials at the login endpoint [`POST /api/auth/login`].
2. Copy the token from the response.
3. Send it with every protected request:

```
Authorization: Bearer <your-token>
```

Requests without a valid token are rejected by `JwtAuthenticationFilter`.

---

## API endpoints

> These routes are examples. Replace them with the actual paths from your controllers.

### Patients
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/patients` | List all patients |
| GET | `/api/patients/{id}` | Get a patient by ID |
| POST | `/api/patients` | Create a patient |
| PUT | `/api/patients/{id}` | Update a patient |
| DELETE | `/api/patients/{id}` | Delete a patient |

### Doctors
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/doctors` | List all doctors |
| GET | `/api/doctors/{id}` | Get a doctor by ID |
| POST | `/api/doctors` | Create a doctor |
| PUT | `/api/doctors/{id}` | Update a doctor |
| DELETE | `/api/doctors/{id}` | Delete a doctor |

### Appointments
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/appointments` | List appointments (supports filtering) |
| GET | `/api/appointments/{id}` | Get an appointment by ID |
| POST | `/api/appointments` | Book an appointment |
| PUT | `/api/appointments/{id}` | Update an appointment |
| DELETE | `/api/appointments/{id}` | Cancel an appointment |

### Prescriptions
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/prescriptions` | List prescriptions |
| GET | `/api/prescriptions/{id}` | Get a prescription by ID |
| POST | `/api/prescriptions` | Create a prescription |
| PUT | `/api/prescriptions/{id}` | Update a prescription |
| DELETE | `/api/prescriptions/{id}` | Delete a prescription |

### Example request

```http
POST /api/appointments
Authorization: Bearer <your-token>
Content-Type: application/json

{
  "patientId": 1,
  "doctorId": 1,
  "appointmentDate": "2026-03-01T10:00:00"
}
```

[Replace with a real request body from your DTOs.]

---

## Project structure

```
src/main/java/com/[your.package]
├── controller/      # REST controllers
├── service/         # service interfaces and implementations
├── repository/      # Spring Data JPA repositories
├── model/           # JPA entities
├── dto/             # request/response DTOs
├── security/        # JwtUtil, JwtAuthenticationFilter, SecurityConfig
└── [Application].java
src/main/resources/
└── application.properties
```

[Adjust to match your real folder layout.]

---

## Design decisions

- **`@RestController` over `@Controller`** – returns JSON directly without view resolution
- **Service interfaces** – keep controllers decoupled from implementations and easier to test
- **JWT over server-side sessions** – stateless, which suits microservices
- **Caching with `@Cacheable` / `@CachePut`** – avoids repeated database reads for frequently requested data
- **DTOs** – keep internal entities out of the API contract

---

## Future improvements

- Role-based access control (admin / doctor / receptionist)
- Persistent database (PostgreSQL) with Flyway migrations
- Unit and integration tests
- Dockerfile for containerized deployment

---

## Screenshots

Add Postman or Swagger screenshots here:

```
![Swagger UI](docs/swagger.png)
```

---

## Author

**Abdallah Hamdan**
Computer Science, Al-Hussein Technical University (HTU)
Backend – Microservices apprentice, Acabes

## License

Add a license of your choice (for example MIT).
