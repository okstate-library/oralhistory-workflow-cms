package com.okstatelibrary.oralhistoryworkflowcms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Collection;

public interface CollectionRepository extends JpaRepository<Collection, Short> {

    List<Collection> findAll();
    
    Optional<Collection> findById(short Id);
}