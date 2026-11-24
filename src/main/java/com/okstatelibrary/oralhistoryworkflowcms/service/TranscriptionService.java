package com.okstatelibrary.oralhistoryworkflowcms.service;

import java.util.List;
import java.util.Optional;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Transcription;
import com.okstatelibrary.oralhistoryworkflowcms.enums.FindReplaceOption;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TranscriptionService {

	List<Transcription> findRecentlyAdded(long userId);

	List<Transcription> findAll();

	List<Transcription> findAll(String ids);

	List<Transcription> findPublic();
	
	List<Transcription> findAll(boolean isSentOut);

	List<Transcription> findAll(String collectionIds, String seriesoptions, String intervieweroptions,
			String subjects, String darkarchive, String onlineoffline);

	Optional<Transcription> findById(int id);

	Transcription save(Transcription transcription);

	List<Transcription> findByCollectinoId(short collectionId);

	List<Transcription> findBySeriesId(int id);

	List<Transcription> findByTranscriberName(String transcriberName);

	List<Transcription> findByInterviewerName(String interviewerName);

	boolean isRecordsExistsBySuject(String subjectName);

	boolean isRecordsExistsByEquipmentName(String equipmentName);

	boolean isRecordsExistsByOriginalMedium(String medium);

	List<Transcription> report(String interviewer, String location, String beginDate, String endDate, String status,
			String bornDigital, String converetd);

	boolean isProjectCodeExists(String code);

	void delete(int id);

	int findReplace(FindReplaceOption option, String find, String replace);

}