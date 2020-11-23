package com.okstatelibrary.oralhistoryworkflowcms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.okstatelibrary.oralhistoryworkflowcms.entity.PredefinedUser;

public interface PredefineRepository extends JpaRepository<PredefinedUser, Short> {

    List<PredefinedUser> findAll();
    
    Optional<PredefinedUser> findById(short Id);
}