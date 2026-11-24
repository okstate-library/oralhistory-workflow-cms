package com.okstatelibrary.oralhistoryworkflowcms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.okstatelibrary.oralhistoryworkflowcms.entity.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TranscriptionRepository extends JpaRepository<Transcription, Integer> {

	List<Transcription> findAll();

	@Query(nativeQuery = true, value = "call get_transcripts_by_ids(:id_list)")
	List<Transcription> getTranscriptsByIds(@Param("id_list") String id_list);
}