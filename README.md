![Java CI with Gradle](https://github.com/timakden/bank-account-management/workflows/Java%20CI%20with%20Gradle/badge.svg?branch=develop)

# Bank account management system

Simple bank account management system to practice Kotlin and Spring Boot.

## Technological stack

* [JDK 17](https://www.oracle.com/java/technologies/javase/17-relnote-issues.html)
* [Kotlin](https://kotlinlang.org/)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Reactor](https://projectreactor.io/)

## Configuration parameters

| Parameter                 | Description                           | Default Value                            |
|---------------------------|---------------------------------------|------------------------------------------|
| `BANK_PORT`               | Application server port               | 8080                                     |
| `BANK_DB_URL`             | PostgreSQL DB URL                     | jdbc:postgresql://localhost:5432/bank-db |
| `BANK_DB_USER`            | PostgreSQL DB user name               | postgres                                 |
| `BANK_DB_PASSWORD`        | PostgreSQL DB user password           | postgres                                 |
| `BANK_LIQUIBASE_USER`     | Liquibase PostgreSQL DB user name     | postgres                                 |
| `BANK_LIQUIBASE_PASSWORD` | Liquibase PostgreSQL DB user password | postgres                                 |
| `BANK_LOG_LEVEL`          | Logging level of bank-app             | info                                     |

## How to run

1. Build application using Gradle: `./gradlew clean build`
2. Run provided Docker Compose file: `docker-compose up -d`
3. The application will be started on port 8080
