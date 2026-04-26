# Secure REST API Demo

A Spring Boot project demonstrating how to secure REST API endpoints using **Spring Security**, with role-based authentication and authorization. CRUD operations are performed on an employee directory, with users and roles managed via a PostgreSQL database.

>  This project is based on the [Spring & Hibernate for Beginners](https://www.udemy.com/course/spring-hibernate-tutorial/) course by **Chád Darby** on Udemy.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 23 |
| Framework | Spring Boot 4.x |
| Security | Spring Security (JDBC authentication) |
| ORM | Spring Data JPA / Hibernate |
| Database | PostgreSQL |
| Build Tool | Maven 3.9.x |
| API Docs | SpringDoc OpenAPI (Swagger UI) |

---

## Features

- Secured REST endpoints using HTTP Basic Authentication
- Role-based authorization (`EMPLOYEE`, `MANAGER`, `ADMIN`)
- JDBC-based user and role management (no hardcoded credentials)
- Full CRUD operations on an Employee entity
- Swagger UI for API exploration

---

## Prerequisites

Make sure you have the following installed:

- Java 23
- Maven 3.9+
- PostgreSQL 15+

---

## Database Setup

### 1. Connect to PostgreSQL and create user and database

```bash
sudo -u postgres psql
```

```sql
CREATE USER springstudent WITH PASSWORD 'springstudent';
CREATE DATABASE employee_directory OWNER springstudent;
GRANT ALL PRIVILEGES ON DATABASE employee_directory TO springstudent;
\q
```

### 2. Connect as the new user

```bash
psql -U springstudent -d employee_directory -h localhost -W
```

### 3. Create tables and seed data

```sql
-- Employee table
CREATE TABLE employee (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(45) DEFAULT NULL,
    last_name  VARCHAR(45) DEFAULT NULL,
    email      VARCHAR(45) DEFAULT NULL
);

-- Spring Security required tables
CREATE TABLE users (
    username VARCHAR(50)  NOT NULL PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    enabled  BOOLEAN      NOT NULL
);

CREATE TABLE authorities (
    username  VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT fk_authorities FOREIGN KEY (username) REFERENCES users(username)
);

-- Seed employees
INSERT INTO employee (id, first_name, last_name, email) VALUES
    (1, 'Leslie', 'Andrews',    'leslie@luv2code.com'),
    (2, 'Emma',   'Baumgarten', 'emma@luv2code.com'),
    (3, 'Avani',  'Gupta',      'avani@luv2code.com'),
    (4, 'Yuri',   'Petrov',     'yuri@luv2code.com'),
    (5, 'Juan',   'Vega',       'juan@luv2code.com');

SELECT setval('employee_id_seq', (SELECT MAX(id) FROM employee));

-- Seed users
INSERT INTO users VALUES ('john',  '{noop}test123', true);
INSERT INTO users VALUES ('mary',  '{noop}test123', true);
INSERT INTO users VALUES ('susan', '{noop}test123', true);

-- Assign roles
INSERT INTO authorities VALUES ('john',  'ROLE_EMPLOYEE');
INSERT INTO authorities VALUES ('mary',  'ROLE_EMPLOYEE');
INSERT INTO authorities VALUES ('mary',  'ROLE_MANAGER');
INSERT INTO authorities VALUES ('susan', 'ROLE_EMPLOYEE');
INSERT INTO authorities VALUES ('susan', 'ROLE_MANAGER');
INSERT INTO authorities VALUES ('susan', 'ROLE_ADMIN');
```

---

## Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.application.name=secure-api-demo
spring.main.banner-mode=off

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/employee_directory
spring.datasource.username=springstudent
spring.datasource.password=springstudent

# JPA
spring.jpa.open-in-view=false
```

---

## Running the Project

```bash
# Clone the repository
git clone <your-repo-url>
cd secure-api-demo

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The app starts at `http://localhost:8080`.

---

## API Endpoints

| Method | Endpoint | Required Role |
|--------|----------|--------------|
| GET | `/api/employees` | EMPLOYEE |
| GET | `/api/employees/{id}` | EMPLOYEE |
| POST | `/api/employees` | MANAGER |
| PUT | `/api/employees` | MANAGER |
| PATCH | `/api/employees/{id}` | MANAGER |
| DELETE | `/api/employees/{id}` | ADMIN |

---

## User Credentials

| Username | Password | Roles |
|----------|----------|-------|
| john | test123 | EMPLOYEE |
| mary | test123 | EMPLOYEE, MANAGER |
| susan | test123 | EMPLOYEE, MANAGER, ADMIN |

---

## Testing with curl

```bash
# GET all employees (john, mary, or susan)
curl -u john:test123 http://localhost:8080/api/employees

# GET single employee
curl -u john:test123 http://localhost:8080/api/employees/1

# POST new employee (manager or above)
curl -u mary:test123 -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"firstName": "Ray", "lastName": "Liotta", "email": "ray.l@demo.com"}'

# DELETE employee (admin only)
curl -u susan:test123 -X DELETE http://localhost:8080/api/employees/6
```

---

## Swagger UI

API documentation is available at:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Project Structure

```
src/main/java/com/biswacodes/secure_api_demo/
├── SecureApiDemoApplication.java
├── controller/
│   └── EmployeeRestController.java
├── entity/
│   └── Employee.java
├── repository/
│   └── EmployeeRepository.java
├── service/
│   ├── EmployeeService.java
│   └── EmployeeServiceImpl.java
└── security/
    └── DemoSecurityConfig.java
```

---

## Credits

Based on the Udemy course [Spring & Hibernate for Beginners (includes Spring Boot)](https://www.udemy.com/course/spring-hibernate-tutorial/) by **Chad Darby**.