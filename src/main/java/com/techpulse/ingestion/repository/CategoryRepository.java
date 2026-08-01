package com.techpulse.ingestion.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techpulse.ingestion.model.Article;
import com.techpulse.ingestion.model.Category;
import com.techpulse.ingestion.model.Source;

public interface CategoryRepository extends JpaRepository<Category,Integer>
    {

    Optional<Article> findById(Long categoryId);

    Optional<Source> findByName(String string);

    }

