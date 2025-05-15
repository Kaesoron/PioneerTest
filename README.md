# PioneerTest 🏦

Spring Boot-приложение для управления банковскими счетами, поддерживает переводы, начисление процентов, безопасность на основе JWT, и использует PostgreSQL + Liquibase для миграций.

## 🔧 Стек технологий

- **Java 21**
- **Spring Boot 3.4.5**
- **Spring Data JPA**
- **Spring Security + OAuth2 Resource Server**
- **PostgreSQL**
- **Liquibase**
- **Docker + Docker Compose**
- **Testcontainers (интеграционные тесты)**
- **JUnit 5, Mockito**
- **Swagger (SpringDoc)**

---

## 🚀 Запуск проекта

### 1. Сборка JAR-файла

```bash
mvn clean package

    Готовый JAR будет в target/PioneerTest-1.0-SNAPSHOT.jar.

2. Запуск через Docker Compose

docker-compose up --build

Это:

    Соберёт Docker-образ приложения

    Запустит PostgreSQL

    Подождёт, пока база будет готова

    Запустит app.jar

Приложение будет доступно на http://localhost:8080
3. REST API

Документация Swagger:

http://localhost:8080/swagger-ui.html

Примеры эндпоинтов:

    POST /api/transfer — перевод средств

    GET /api/accounts — получить список аккаунтов (если реализовано)

🧪 Запуск тестов

mvn test

Для интеграционных тестов используется Testcontainers, PostgreSQL запускается в контейнере автоматически.
