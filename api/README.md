# API

A Spring Rest API project.

## Implementation details

- Spring Boot
- Spring Web MVC
- Spring Security and [JWT](https://jwt.io/)

## Requirements

- Download and install [Maven](https://maven.apache.org/download.cgi) >= 3

- Download and install [Java](https://www.oracle.com/java/technologies/javase/jdk14-archive-downloads.html) >= 14

- A MongoDB instance.

## Provision demo data

Create a **ecommerce** MongoDB database.

Run the operations from **collections-demo** folder.

## Build application

```sh
mvn clean install
```

## Run application

```sh
java -jar target/e-commerce-0.0.1-SNAPSHOT.jar
```

## API Documentation

- Swagger: http://localhost:8080/api/swagger-ui.html#/
