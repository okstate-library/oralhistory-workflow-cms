package com.okstatelibrary.oralhistoryworkflowcms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.okstatelibrary.oralhistoryworkflowcms.entity.*;

public interface TranscriptionRepository extends JpaRepository<Transcription, Long> {

	List<Transcription> findAll();
}