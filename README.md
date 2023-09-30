# Xpand IT Backend Challenge

## Table of Contents
- [Setting Up](#setting-up)
  - [Docker](#docker)
  - [PostgresSQL Database Configuration](#PostgreSQLDatabaseConfiguration)
  - [IntelliJ](#intellij)
- [Populate the Database](#populate-the-database)
- [Swagger](#swagger)

## Setting Up

### Docker

Open your terminal and execute the following command to build the docker environment.

```
docker compose up
```

**NOTE**: the postgres database will run on port 5432.

### PostgreSQL Database Configuration

The PostgreSQL database used in this project can be configured using the following environment variables in the `docker-compose.yml` file:

- `POSTGRES_USER`: The PostgreSQL username (default: `postgres`).
- `POSTGRES_PASSWORD`: The PostgreSQL password (default: `postgres`).
- `POSTGRES_DB`: The name of the PostgreSQL database (default: `BackendChallenge`).

You can customize these variables in the `docker-compose.yml` file to match your specific requirements.

### IntelliJ

- Java version: openjdk-20 Oracle OpenJDK version 20.0.2

### Populate the Database

To populate the database, follow these steps: 

- Locate and run the `populate_db.sql` file, which can be found inside the `init-scripts-db` folder.

This will execute the SQL script and populate the database with the necessary data.

### Swagger

- Swagger Ui: [Swagger Docs](http://localhost:8080/swagger-ui/index.html#/)

