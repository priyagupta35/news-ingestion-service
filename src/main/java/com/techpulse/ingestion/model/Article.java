package com.techpulse.ingestion.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "url", length = 2000)
    private String url;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Source source;

    // Plain String — matches ENUM column in MySQL directly
    @Column(name = "type",
        columnDefinition = "ENUM('EXTERNAL','COMMUNITY')")
    private String type;

    // Plain String — matches ENUM column in MySQL directly
    @Column(name = "status",
        columnDefinition = "ENUM('PENDING','APPROVED','REJECTED')")
    private String status;

    public Article() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public LocalDateTime getPublishedAt() { return publishedAt; }
    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Category getCategory() { return category; }
    public void setCategory(Category category) {
        this.category = category;
    }

    public Source getSource() { return source; }
    public void setSource(Source source) { this.source = source; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}