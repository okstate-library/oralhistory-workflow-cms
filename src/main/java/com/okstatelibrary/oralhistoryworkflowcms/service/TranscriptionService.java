package com.okstatelibrary.oralhistoryworkflowcms.service;

import java.util.List;
import java.util.Optional;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Transcription;
import com.okstatelibrary.oralhistoryworkflowcms.enums.FindReplaceOption;

public interface TranscriptionService {

	List<Transcription> findRecentlyAdded(long userId);

	List<Transcription> findAll();

	List<Transcription> findAll(boolean isTranscripted);

	List<Transcription> findAll(String collectionIds, String subseriesoptions, String intervieweroptions,
			String subjects, String darkarchive, String onlineoffline);

	List<Transcription> findAll(boolean isTranscripted, boolean isPriority, boolean isTranscribed, boolean isAuditCheck,
			boolean isFirstEdit, boolean isSecondEdit, boolean isDraftSent, boolean isCorrections, boolean isFinalEdit,
			boolean isSsentOut);

	Optional<Transcription> findById(int id);

	Transcription save(Transcription transcription);

	List<Transcription> findByCollectinoId(short collectionId);

	List<Transcription> findBySubseriesId(int id);

	List<Transcription> findByTranscriberName(String transcriberName);

	List<Transcription> findByInterviewerName(String interviewerName);

	List<Transcription> findBySuject(String subjectName);

	List<Transcription> findByEquipmentName(String equipmentName);

	List<Transcription> report(String interviewer, String location, String beginDate, String endDate, String status,
			String bornDigital, String converetd);

	void delete(int id);

	int findreplace(FindReplaceOption option, String find, String replace);

}