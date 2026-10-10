<div align="center">

# News Ingestion Service 🔄
### Automated Tech News Ingestion Pipeline for TechPulse

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Data JPA](https://img.shields.io/badge/Spring_Data-JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-data-jpa)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Neon_Cloud-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://neon.tech/)
[![Docker](https://img.shields.io/badge/Docker-Containerized-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![Render](https://img.shields.io/badge/Render-Deployed-46E3B7?style=for-the-badge&logo=render&logoColor=black)](https://render.com/)

<p align="center">
  A dedicated data ingestion microservice for the <b><a href="https://github.com/priyagupta35/techpulse">TechPulse</a></b> platform.<br/>
  Periodically fetches live technology headlines from <b>NewsAPI</b> every 30 minutes, applies URL-level deduplication, resolves publisher sources, and persists structured articles into a shared <b>Neon PostgreSQL</b> database.
</p>

---

</div>

## 🌐 Live Cloud Deployment

| Service Component | Endpoint URL | Status |
| :--- | :--- | :--- |
| **Service Root** | [`https://news-ingestion-service-3.onrender.com/`](https://news-ingestion-service-3.onrender.com/) | `200 OK` |
| **Health Check** | [`https://news-ingestion-service-3.onrender.com/api/ingestion/health`](https://news-ingestion-service-3.onrender.com/api/ingestion/health) | `200 OK` |
| **Manual Trigger** | [`POST https://news-ingestion-service-3.onrender.com/api/ingestion/fetch`](https://news-ingestion-service-3.onrender.com/api/ingestion/fetch) | `Authenticated / Admin` |

> ℹ️ **Note:** Hosted on Render Free Tier. If inactive, the service may take 30–60 seconds to spin up on first request.

---

## 📌 Role in the TechPulse Ecosystem

```
  ┌────────────────────────┐
  │      NewsAPI.org       │
  └───────────┬────────────┘
              │ (every 30 mins / cron)
              ▼
  ┌──────────────────────────────────────────────────┐
  │           News Ingestion Service (Port 8081)     │  <-- THIS SERVICE
  │   • Scheduled polling via @Scheduled             │
  │   • Duplicate detection via existsByUrl()        │
  │   • Source normalization (findOrCreateSource)    │
  └───────────────────┬──────────────────────────────┘
                      │ writes
                      ▼
  ┌──────────────────────────────────────────────────┐
  │        PostgreSQL Database (Neon Cloud)          │
  └───────────────────▲──────────────────────────────┘
                      │ reads
  ┌───────────────────┴──────────────────────────────┐
  │         News Delivery Service (Port 8080)        │
  │   • User REST APIs & JWT Security                │
  │   • AI Summaries via DeepSeek / Ollama           │
  └──────────────────────────────────────────────────┘
```

The News Ingestion Service operates completely decoupled from user-facing read traffic:
- **Zero Impact on Read Latency:** Data scraping and external HTTP requests never block end-user article queries.
- **Shared Data Layer:** Ingested articles are written directly to PostgreSQL (Neon Cloud), immediately becoming available to [News Delivery Service](https://github.com/priyagupta35/news-delivery-service).
- **Inter-Service Communication:** News Delivery Service can trigger on-demand scraping by calling `POST /api/ingestion/fetch` over internal REST.

---

## ⚙️ Ingestion Engine Workflow

Every 30 minutes (`fixedRate = 1800000` ms), the `NewsIngestionService` executes:

1. **HTTP Ingestion:** Queries NewsAPI top headlines for technology (`category=technology&country=us`) using Spring `RestTemplate`.
2. **Data Sanitation & Filter:**
   - Filters out removed, null, or corrupt article payloads (`[Removed]`).
   - Validates article URLs.
3. **Deduplication:** Checks the database via `articleRepository.existsByUrl(url)` before saving to prevent duplicate entries.
4. **Source Resolution:** Finds existing news outlets or automatically provisions new entries via `findOrCreateSource()`.
5. **Entity Persistence:** Saves the structured entity (`Article`) with default category, external source, timestamp, and status `APPROVED`.

---

## 🔌 API Specification

| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `GET` | `/` | Service status overview | Public |
| `GET` | `/api/ingestion/health` | Health check endpoint for uptime monitoring | Public |
| `GET` | `/api/ingestion/articles` | Retrieve all ingested articles directly | Public |
| `POST` | `/api/ingestion/fetch` | Manually triggers the NewsAPI scraping pipeline | Public / Inter-service |

---

## 🛠️ Tech Stack & Dependencies

- **Language:** Java 17
- **Framework:** Spring Boot 3.5.x (Spring MVC, Spring Data JPA, Spring Scheduling)
- **Database:** PostgreSQL (Neon Serverless Cloud) / Hibernate ORM
- **HTTP Client:** Spring `RestTemplate`
- **External API:** NewsAPI.org
- **Containerization:** Docker (Multi-stage Eclipse Temurin JRE build)
- **Cloud Hosting:** Render Cloud Platform

---

## 🚀 Local Setup & Configuration

### Prerequisites
- **JDK 17 or higher**
- **Maven 3.8+** (or included `./mvnw`)
- **PostgreSQL** database (Local or Neon Cloud)
- **NewsAPI Key** (Free from [newsapi.org](https://newsapi.org))

### 1. Clone the Repository
```bash
git clone https://github.com/priyagupta35/news-ingestion-service.git
cd news-ingestion-service
```

### 2. Configure `application.properties`
Copy the sample properties:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Configure your environment variables / properties:
```properties
spring.application.name=news-ingestion-service
server.port=${PORT:8081}

# PostgreSQL Database
spring.datasource.url=jdbc:postgresql://your-neon-host/neondb?sslmode=require
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update

# NewsAPI Configuration
newsapi.key=your_newsapi_key
newsapi.url=https://newsapi.org/v2/top-headlines?country=us&category=technology&apiKey=
```

### 3. Run Locally
```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

Service starts at `http://localhost:8081`.

### 4. Run with Docker
```bash
docker build -t news-ingestion-service .
docker run -p 8081:8081 \
  -e PORT=8081 \
  -e SPRING_DATASOURCE_URL="jdbc:postgresql://your-neon-host/neondb?sslmode=require" \
  -e SPRING_DATASOURCE_USERNAME="your_username" \
  -e SPRING_DATASOURCE_PASSWORD="your_password" \
  -e NEWSAPI_KEY="your_newsapi_key" \
  news-ingestion-service
```

---

## 📁 Connected Repositories in the Ecosystem

| Repository | Architecture & Purpose |
| :--- | :--- |
| **[techpulse](https://github.com/priyagupta35/techpulse)** | The original monolithic Spring Boot codebase, full domain architecture, and MySQL schema. |
| **[news-delivery-service](https://github.com/priyagupta35/news-delivery-service)** | User-facing microservice with JWT authentication, RBAC, and Spring AI (DeepSeek) article summarization. |
| **[techpulse-docker](https://github.com/priyagupta35/techpulse-docker)** | Multi-container Docker Compose configuration spinning up MySQL and both microservices locally. |

---

## 👩‍💻 Author

**Priya Gupta**  
*B.Tech in Computer Science & Business Systems (CSBS)*  
*Netaji Subhash Engineering College, Kolkata*

- **GitHub:** [@priyagupta35](https://github.com/priyagupta35)
- **LinkedIn:** [Priya Gupta](https://www.linkedin.com/in/priya-gupta-b4a79528a/)
- **Email:** [priyagupta.csbs@gmail.com](mailto:priyagupta.csbs@gmail.com)
