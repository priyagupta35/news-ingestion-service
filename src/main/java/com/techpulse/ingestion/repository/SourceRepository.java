package com.techpulse.ingestion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;

import com.techpulse.ingestion.model.Source;

public interface SourceRepository
 extends JpaRepository<Source,Integer>{

    Optional<Source> findByName(String name);

}
