package com.techpulse.ingestion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techpulse.ingestion.service.NewsIngestionService;

@RestController
@RequestMapping("/api/ingestion")
public class IngestionController {

    @Autowired
    private NewsIngestionService newsIngestionService;

    // GET /api/ingestion/health
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok(
            "News Ingestion Service is running");
    }

    @Autowired
private ArticleRepository articleRepository; // (or whatever your repository is named)
// GET /api/ingestion/articles
@GetMapping("/articles")
public ResponseEntity<?> getArticles() {
    return ResponseEntity.ok(articleRepository.findAll());
}

    // POST /api/ingestion/fetch
    @PostMapping("/fetch")
    public ResponseEntity<String> fetchNews() {
        newsIngestionService.fetchAndStoreArticles();
        return ResponseEntity.ok(
            "News ingestion triggered successfully");
    }
}