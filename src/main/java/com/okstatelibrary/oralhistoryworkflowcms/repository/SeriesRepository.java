package com.okstatelibrary.oralhistoryworkflowcms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Series;

public interface SeriesRepository extends JpaRepository<Series, Integer> {

    List<Series> findAll();
    
    Optional<Series> findById(int Id);
}