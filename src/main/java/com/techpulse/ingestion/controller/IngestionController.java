package com.techpulse.ingestion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techpulse.ingestion.service.NewsIngestionService;

@RestController
@RequestMapping("/api/ingestion")
public class IngestionController {
    
    @Autowired
    private NewsIngestionService newsIngestionService;

    public IngestionController(NewsIngestionService newsIngestionService) {
        this.newsIngestionService = newsIngestionService;
    }

    @PostMapping("/fetch")
    public ResponseEntity<String> fetchNews() {
        newsIngestionService.fetchAndStoreArticles();
        return ResponseEntity.ok( "News ingestion triggered successfully");
    }


    //health check endpoint to confirm service is running
    @GetMapping("/health")
    public ResponseEntity<String> health(){
        return ResponseEntity.ok( "News Ingestion Service is running ");
    }

    public NewsIngestionService getNewsIngestionService() {
        return newsIngestionService;
    }

    public void setNewsIngestionService(NewsIngestionService newsIngestionService) {
        this.newsIngestionService = newsIngestionService;
    }
}
