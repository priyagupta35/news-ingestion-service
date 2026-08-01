package com.techpulse.ingestion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.techpulse.ingestion.model.Article;

public interface ArticleRepository 
    extends JpaRepository<Article, Integer> {

    // Finds articles by category ID
    List<Article> findByCategoryId(int categoryId);

    // Finds articles by status string — APPROVED, PENDING, REJECTED
    // status is now a plain String not an enum
    List<Article> findByStatus(String status);

    // Checks if article with this URL already exists
    // Used by ingestion service to prevent duplicates
    boolean existsByUrl(String url);

    // findById, deleteById, save, findAll etc. are all
    // already provided by JpaRepository — never redeclare them
}