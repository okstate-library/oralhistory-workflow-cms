package com.okstatelibrary.oralhistoryworkflowcms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Medium;

public interface MediumRepository extends JpaRepository<Medium, Short> {

	List<Medium> findAll();
}