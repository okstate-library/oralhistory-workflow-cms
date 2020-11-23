package com.okstatelibrary.oralhistoryworkflowcms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Short> {

    List<Subject> findAll();
    
    Optional<Subject> findById(short id);
}