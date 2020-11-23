package com.okstatelibrary.oralhistoryworkflowcms.service;

import java.util.List;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Transcription;

public interface TranscriptionService {

	List<Transcription> findRecentlyAdded(long userId);
	
	List<Transcription> findAll();
	
	List<Transcription> findAll(String collectionIds);

	List<Transcription> findAll(boolean isTranscripted, boolean isPriority, boolean isTranscribed, boolean isAuditCheck, boolean isFirstEdit,
			boolean isSecondEdit, boolean isDraftSent, boolean isCorrections, boolean isFinalEdit, boolean isSsentOut);

}