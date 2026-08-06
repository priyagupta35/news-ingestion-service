# News Ingestion Service

## Overview

The News Ingestion Service is a Spring Boot microservice responsible for periodically fetching the latest technology news from NewsAPI and persisting it to the database.
It serves as the data ingestion layer of the TechPulse platform, ensuring that the latest articles are continuously available for downstream services.

## Features

- Fetches technology news from NewsAPI
- Scheduled data ingestion
- Stores articles in MySQL
- Prevents duplicate article insertion
- Logging using Log4j2
- REST endpoint for manual ingestion (if enabled)

## Technology Stack

- Java 21
- Spring Boot
- Spring Data JPA (Hibernate)
- MySQL
- Maven
- Log4j2
- NewsAPI

## Related Service

This service supplies article data to the **News Delivery Service**, which exposes secure REST APIs and AI-powered summarization.

**Repository:**  
`https://github.com/priyagupta35/news-delivery-service`
