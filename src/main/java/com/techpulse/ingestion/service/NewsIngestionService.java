package com.techpulse.ingestion.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.techpulse.ingestion.dto.NewsApiResponse;
import com.techpulse.ingestion.dto.NewsArticleDto;
import com.techpulse.ingestion.model.Article;
import com.techpulse.ingestion.model.Category;
import com.techpulse.ingestion.model.Source;
import com.techpulse.ingestion.repository.ArticleRepository;
import com.techpulse.ingestion.repository.CategoryRepository;
import com.techpulse.ingestion.repository.SourceRepository;

@Service
public class NewsIngestionService {

    private static final Logger logger =
        LogManager.getLogger(NewsIngestionService.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Fix — added missing @Autowired
    @Autowired
    private SourceRepository sourceRepository;

    // Fix — removed final keyword so @Value can inject
    @Value("${newsapi.key}")
    private String newsApiKey;

    @Value("${newsapi.url}")
    private String newsApiUrl;

    private RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 1800000)
    public void fetchAndStoreArticles() {
        logger.info("News ingestion started at: {}",
            LocalDateTime.now());
        try {
            String fullUrl = newsApiUrl + newsApiKey;
            logger.debug("Calling NewsAPI");

            NewsApiResponse response = restTemplate.getForObject(
                fullUrl, NewsApiResponse.class);

            if (response != null && response.getArticles() != null) {
                List<NewsArticleDto> articles = response.getArticles();
                int savedCount = 0;
                int skippedCount = 0;

                logger.debug("NewsAPI returned {} articles",
                    articles.size());

                for (NewsArticleDto articleDto : articles) {

                    if (articleDto.getTitle() == null ||
                        articleDto.getTitle().equals("[Removed]")) {
                        logger.warn("Skipping article with null or " +
                            "removed title from source: {}",
                            articleDto.getSource() != null
                            ? articleDto.getSource().getName()
                            : "Unknown");
                        skippedCount++;
                        continue;
                    }

                    if (articleDto.getUrl() == null) {
                        logger.warn("Skipping article with null URL: {}",
                            articleDto.getTitle());
                        skippedCount++;
                        continue;
                    }

                    if (articleRepository.existsByUrl(
                            articleDto.getUrl())) {
                        logger.debug("Skipping duplicate article: {}",
                            articleDto.getTitle());
                        skippedCount++;
                        continue;
                    }

                    String sourceName = (articleDto.getSource() != null
                        && articleDto.getSource().getName() != null)
                        ? articleDto.getSource().getName()
                        : "Unknown";

                    Source source = findOrCreateSource(sourceName);

                    Category category = categoryRepository
                        .findById(1)
                        .orElse(null);

                    if (category == null) {
                        logger.warn("Default category with ID 1 not " +
                            "found. Article will have no category.");
                    }

                    Article article = new Article();
                    article.setTitle(articleDto.getTitle());
                    article.setSummary(articleDto.getDescription());
                    article.setUrl(articleDto.getUrl());
                    article.setPublishedAt(
                        parseDate(articleDto.getPublishedAt()));
                    article.setSource(source);
                    article.setCategory(category);
                    article.setType("EXTERNAL");
                    article.setStatus("APPROVED");

                    articleRepository.save(article);
                    savedCount++;

                    logger.debug("Saved article: {}",
                        articleDto.getTitle());
                }

                logger.info("News ingestion complete. Saved: {}, " +
                    "Skipped: {}", savedCount, skippedCount);

            } else {
                logger.warn("NewsAPI returned null or empty response");
            }

        } catch (Exception e) {
            logger.error("News ingestion failed: {}",
                e.getMessage(), e);
        }
    }

    private Source findOrCreateSource(String sourceName) {
        return sourceRepository.findByName(sourceName)
            .orElseGet(() -> {
                logger.debug("Creating new source: {}", sourceName);
                Source newSource = new Source();
                newSource.setName(sourceName);
                newSource.setWebsiteUrl("");
                newSource.setCountry("Unknown");
                return sourceRepository.save(newSource);
            });
    }

    private LocalDateTime parseDate(String dateString) {
        if (dateString == null) return LocalDateTime.now();
        try {
            return LocalDateTime.parse(dateString,
                DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            logger.warn("Could not parse date: {}. Using current time.",
                dateString);
            return LocalDateTime.now();
        }
    }
}