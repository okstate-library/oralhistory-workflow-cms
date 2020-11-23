package com.okstatelibrary.oralhistoryworkflowcms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Repository;

public interface RepositoryRepository extends JpaRepository<Repository, Short> {

    List<Repository> findAll();
    
    Optional<Repository> findById(short Id);
}