# News Ingestion Service

Part of the [TechPulse](https://github.com/priyagupta35/techpulse) 
microservices platform.

---

## 🌐 Live Deployment https://news-ingestion-service-3.onrender.com

**Health Check**
GET https://news-ingestion-service-3.onrender.com/api/ingestion/health

**Manual Fetch Trigger**
POST https://news-ingestion-service-3.onrender.com/api/ingestion/fetch


---

## What This Service Does

The News Ingestion Service is responsible for automatically 
fetching live technology articles from NewsAPI every 30 minutes 
and persisting them to the shared PostgreSQL database. It serves 
as the data ingestion layer of the TechPulse platform, ensuring 
the latest articles are continuously available for the News 
Delivery Service.

---

## 🛠 Tech Stack

| Category | Technologies |
|----------|-------------|
| Language | Java 17 |
| Framework | Spring Boot 3.x, Spring Data JPA |
| Database | PostgreSQL (Neon Cloud), Hibernate ORM |
| Scheduling | Spring Scheduler |
| Logging | Log4j2 |
| Containerisation | Docker |
| Deployment | Render Cloud Platform |
| External API | NewsAPI |
| Build Tool | Maven |

---

## 🔌 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/ingestion/health` | Health check |
| POST | `/api/ingestion/fetch` | Manually trigger news fetch |

---

## ⚙️ Local Setup

### Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL or Neon account
- NewsAPI key from newsapi.org

### Configure application.properties
```properties
spring.application.name=news-ingestion-service
server.port=8081

spring.datasource.url=jdbc:postgresql://your-neon-host/neondb?sslmode=require
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

newsapi.key=your_newsapi_key
newsapi.url=https://newsapi.org/v2/top-headlines?country=us&category=technology&apiKey=
```

### Run locally
```bash
mvn spring-boot:run
```

### Run with Docker
```bash
docker build -t news-ingestion-service .
docker run -p 8081:8081 news-ingestion-service
```

---

## How It Works

Every 30 minutes Spring Scheduler triggers fetchAndStoreArticles 
which calls the NewsAPI endpoint using RestTemplate. Each article 
is checked against existsByUrl to prevent duplicates. New articles 
are mapped to entity objects and saved to the PostgreSQL database. 
If no default category exists the article is saved without one.

---

## 📁 Related Repositories

| Repository | Description |
|-----------|-------------|
| [techpulse](https://github.com/priyagupta35/techpulse) | Original monolith and database schema |
| [news-delivery-service](https://github.com/priyagupta35/news-delivery-service) | User-facing REST APIs and AI summarisation |
