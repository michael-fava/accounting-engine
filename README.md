# Accounting Engine Mini Project
(An initial attempt at Kotlin)

Contact Email: mfava85@gmail.com

## Tech Stack

- Kotlin
- Spring Boot
- Spring Data JPA
- H2 In-Memory Database
- Flyway
- Orika
- Swagger

## Structure

Project is micro-service providing the creation of accounts and transcations of type, DEPOSIT, TRANSFER and WITHDRAW

## Development
________________________________________________________________________________

### Database

The current data model consists of two entities `account` and `transaction_entry`.
Database is implemented using H2 in memory and entities are accessed using Spring Data JPA.
No account or transaction_entry can be deleted once created.


### Api

Api provides endpoints for accounts and transactions at the below BaseURL.

BaseURL: http://localhost:8080

#### Execution

Executes as a spring-boot application on port 8080.

#### Documentation

Swagger documentation providerd @ below url.

http://localhost:8080/swagger-ui.html
