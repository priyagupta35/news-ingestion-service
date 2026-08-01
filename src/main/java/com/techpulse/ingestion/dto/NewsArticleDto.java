
package com.techpulse.ingestion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public
class NewsArticleDto {
    private String title;
    private String description; // maps to our summary field
    private String url;
    private String publishedAt;
    private NewsSourceDto source;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public NewsSourceDto getSource() {
        return source;
    }

    public void setSource(NewsSourceDto source) {
        this.source = source;
    }
}
