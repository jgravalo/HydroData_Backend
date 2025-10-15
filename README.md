# 💧 Repte 3: Gestió sostenible dels recursos hídrics a Catalunya

This repository contains the Spring Boot API developed for managing water consumption data, anomalies, and user authentication. It uses **H2** as an in-memory database for the development phase and follows a **JPA and REST-based architecture** for seamless communication with the frontend.

## 🚀 Getting Started

### Tech Stack

* **Java 21**

* **Maven** 3.5.6

* **Docker** (optional, for containerized deployment)

## ⚙️ Configuration and Database

The application is configured to run on port **9000**.

| Property | Value | Description |
|---|---|---|
| `server.port` | `9000` | Listening port for the API and server. |
| `spring.h2.console.enabled` | `true` | Enables the H2 console for debugging. |
| **H2 Console URL** | `http://localhost:9000/h2-console` | Access the database UI (JDBC URL: `jdbc:h2:mem:testdb`). |

### Data Initialization

Data loading is handled by the **`DataInitializer`** class, which implements `CommandLineRunner`. It reads data from `src/main/resources/json/2015_consum_aigua.json` and automatically persists it to the H2 database upon application startup.

## 🗺️ Project Architecture

The project follows a standard three-tier architecture structured under the main package `hackatonGrupUn.repteTres`:

| Directory | Content                                                  | Description |
|---|----------------------------------------------------------|---|
| `model` | `Data`, `User`, `Anomaly`, `Alert`                       | JPA entities representing the database tables. |
| `repository` | `DataRepository`, `UserRepository`, `AlertRepository`    | Spring Data JPA interfaces for data access operations. |
| `service` | `DataService`, `UserService`, `AuthService`              | Business logic layer; handles operations between controllers and repositories. |
| `controller` | `DataController`, `UserController`, `AnomalyController`. | REST API exposure layer using `@RestController`. |
| `config` | `SecurityConfig`, `WebConfig`                            | Configuration for Spring Security, CORS, and general application beans. |
| `jsonUtils` | `DataInitializer`                                        | Logic dedicated to reading the JSON file and bootstrapping the H2 database. |

## 🔌 API Endpoints

All endpoints are served from the host running on port **9000**.

| Category | Endpoint | HTTP Method | Description | Return Data                  |
|---|---|---|---|------------------------------|
| **Data** | `/hydraulic/api/v1/data` | `GET` | Retrieves all raw consumption and population data from the database. | List of `Data` objects.      |
| **Data** | `/hydraulic/api/v1/averages` | `GET` | Calculates and returns the average water consumption per district. | List of calculated averages. |
| **User** | `/api/users` | `GET` | Retrieves all registered users (Requires authentication/authorization). | List of `User` objects.      |
| **Auth** | `/login` | `POST` | Authenticates a user with credentials (`LoginRequestDto`). | Success response.            |

## FrontEnd Repository
Here is the frontend repository: https://github.com/JlBestMc/HackathonRepte3_Frontend

## 🧑‍🔧 Team Authors
Alfonso Cocinas -> https://github.com/acocinas

Toni Romero -> https://github.com/ToniR90

Isaac Díez -> https://github.com/isaac-diez

Jesús Grávalos -> https://github.com/jgravalo

Juan Luis Rodríguez -> https://github.com/JlBestMc

Antonio Felices -> https://github.com/antoniofelices

Omar Salvat -> https://github.com/omarsaou
