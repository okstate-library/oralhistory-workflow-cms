package com.okstatelibrary.oralhistoryworkflowcms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Subserie;

public interface SubserieRepository extends JpaRepository<Subserie, Integer> {

    List<Subserie> findAll();
    
    Optional<Subserie> findById(int Id);
}