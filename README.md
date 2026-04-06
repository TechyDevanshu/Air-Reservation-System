# ✈️ Air Reservation System – Spring Boot Microservices

A complete airline booking platform built using Spring Boot Microservices architecture with Service Discovery, API Gateway, Centralized Configuration and Thymeleaf frontend.

---

## 🧱 Architecture

User → API Gateway → Services → Database

* Service Registry (Eureka)
* Config Server (Centralized properties)
* API Gateway (Routing & Security)
* User Service
* Flight Service
* Booking Service
* Report Service
* Frontend Service (Thymeleaf UI)

---

## 🛠 Tech Stack

**Backend**

* Java 17
* Spring Boot
* Spring Cloud
* Spring Security + JWT
* Eureka Discovery Server
* Spring Cloud Config
* OpenFeign / WebClient

**Frontend**

* Thymeleaf
* HTML / CSS / JS

**Database**

* H2 Database

---

## 🚀 How to Run the Project

Start services in order:

1. config-server
2. service-registry (Eureka)
3. api-gateway
4. all microservices
5. frontend-service

Access application:
http://localhost:8085/auth/login

---

## 📌 Features

* User Authentication & Authorization
* Flight Search
* Ticket Booking
* Booking History
* Admin Flight Management
* Centralized Configuration
* Service Discovery
* API Gateway Routing

---

## 👨‍💻 Author

**Devanshu Urmaliya**
