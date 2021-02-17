package com.okstatelibrary.oralhistoryworkflowcms.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Transcription;
import com.okstatelibrary.oralhistoryworkflowcms.repository.TranscriptionRepository;
import com.okstatelibrary.oralhistoryworkflowcms.service.TranscriptionService;
import com.okstatelibrary.oralhistoryworkflowcms.util.StringUtil;

@Service
public class TranscriptionServiceImpl implements TranscriptionService {

	@Autowired
	private TranscriptionRepository transcriptionRepository;

	public List<Transcription> findAll() {
		return transcriptionRepository.findAll();
	}

	@Override
	public List<Transcription> findAll(boolean isTranscripted) {
		return transcriptionRepository.findAll().stream()
				.filter(u -> u.getTranscriptStatus() == (byte) (isTranscripted ? 1 : 0)).collect(Collectors.toList());
	}

	@Override
	public List<Transcription> findAll(boolean isTranscripted, boolean isPriority, boolean isTranscribed,
			boolean isAuditCheck, boolean isFirstEdit, boolean isSecondEdit, boolean isDraftSent, boolean isCorrections,
			boolean isFinalEdit, boolean isSentOut) {

		return transcriptionRepository.findAll().stream()
				.filter(u -> u.getTranscriptStatus() == (byte) (isTranscripted ? 1 : 0))
				.filter(u -> u.getIsPriority() == (byte) (isPriority ? 1 : u.getIsPriority()))
				.filter(u -> isTranscribed ? StringUtil.isNotNullOrEmpty(u.getTranscriberAssigned()) : true)
				.filter(u -> isAuditCheck ? StringUtil.isNotNullOrEmpty(u.getAuditCheckCompleted()) : true)
				.filter(u -> isFirstEdit ? StringUtil.isNotNullOrEmpty(u.getFirstEditCompleted()) : true)
				.filter(u -> isSecondEdit ? StringUtil.isNotNullOrEmpty(u.getSecondEditCompleted()) : true)
				.filter(u -> isDraftSent ? u.getDraftSentDate() != null : true)
				.filter(u -> isCorrections ? StringUtil.isNotNullOrEmpty(u.getEditWithCorrectionCompleted()) : true)
				.filter(u -> isFinalEdit ? StringUtil.isNotNullOrEmpty(u.getFinalEditCompleted()) : true)
				.filter(u -> isSentOut ? u.getFinalSentDate() != null : true).collect(Collectors.toList());

	}

	@SuppressWarnings("null")
	@Override
	public List<Transcription> findRecentlyAdded(long userId) {

		List<Transcription> list = this.findAll().stream().filter(u -> u.getCreatedBy() == userId)
				.collect(Collectors.toList());

		if (list != null || list.size() > 0) {

			if (list.size() > 10) {
				return list.subList(0, 9);
			} else {
				return list.subList(0, list.size());
			}
		}

		return null;

	}

	@Override
	public Optional<Transcription> findById(int id) {

		return transcriptionRepository.findById(id);
	}

	@Override
	public Transcription save(Transcription transcription) {
		// TODO Auto-generated method stub
		return transcriptionRepository.save(transcription);
	}

	@Override
	public List<Transcription> findBySubseriesId(int subseriesId) {

		return transcriptionRepository.findAll().stream().filter(u -> u.getSubseriesId() == subseriesId)
				.collect(Collectors.toList());

	}

	@Override
	public List<Transcription> findByTranscriberName(String transcriberName) {

		System.out.println("transcriberName - " + transcriberName);

		return transcriptionRepository.findAll().stream()
				.filter(u -> u.getTranscriberAssigned().contains(transcriberName)).collect(Collectors.toList());

	}

	@Override
	public List<Transcription> findByInterviewerName(String interviewerName) {

		return transcriptionRepository.findAll().stream().filter(u -> u.getInterviewer().contains(interviewerName))
				.collect(Collectors.toList());

	}

	@Override
	public List<Transcription> findBySuject(String subjectName) {
		return transcriptionRepository.findAll().stream().filter(u -> u.getSubject().contains(subjectName))
				.collect(Collectors.toList());

	}

	@Override
	public List<Transcription> findByEquipmentName(String equipmentName) {

		System.out.println("equipmentName" + equipmentName);

		Stream<Transcription> stream = transcriptionRepository.findAll().stream()
				.filter(u -> u.getSubject().contains(equipmentName));

		return stream == null ? null : stream.collect(Collectors.toList());
	}

	@Override
	public List<Transcription> report(String interviewer, String location, String beginDate, String endDate,
			String status, String bornDigital, String converetd) {

		return transcriptionRepository.findAll().stream()
				.filter(getInterviewerPredicate(interviewer).and(getLocationPredicate(location))
						.and(getStatusPredicate(status))
						.and(getBornDigitalPredicate(bornDigital).and(getConveretdPredicate(converetd))))

				.collect(Collectors.toList());

	}

	@Override
	public void delete(int id) {

		Transcription transcription = findById(id).orElse(null);

		transcriptionRepository.delete(transcription);
	}

	private static Predicate<Transcription> getInterviewerPredicate(String interviewer) {
		if (!StringUtils.isEmpty(interviewer)) {
			return u -> u.getInterviewer().toLowerCase().contains(interviewer.toLowerCase());
		}
		return u -> !u.getInterviewer().isEmpty();
	}

	private static Predicate<Transcription> getLocationPredicate(String location) {
		if (!StringUtils.isEmpty(location)) {
			return u -> !StringUtils.isEmpty(u.getTranscriptLocation())
					&& u.getTranscriptLocation().toLowerCase().contains(location.toLowerCase());
		}
		return u -> u.getId() > 0;
	}

	private static Predicate<Transcription> getStatusPredicate(String status) {
		if (!StringUtils.isEmpty(status)) {
			return u -> u.getIsOnline() == Byte.parseByte(status);
		}
		return u -> u.getId() > 0;
	}

	private static Predicate<Transcription> getBornDigitalPredicate(String bornDigital) {
		if (!StringUtils.isEmpty(bornDigital)) {
			return u -> u.getIsBornDigital() == Byte.parseByte("1");
		}
		return u -> u.getId() > 0;
	}

	private static Predicate<Transcription> getConveretdPredicate(String converted) {
		if (!StringUtils.isEmpty(converted)) {
			return u -> u.getIsConvertToDigital() == Byte.parseByte("1");
		}
		return u -> u.getId() > 0;
	}

	@Override
	public List<Transcription> findAll(String collectionIds, String subseriesIds) {

		// return transcriptionRepository.findAll().stream().filter(u ->
		// u.getCollectionId() == 21)
		// .collect(Collectors.toList());

		List<Predicate<Transcription>> collectionPredicates = new ArrayList<Predicate<Transcription>>();
//
//		allPredicates.add(c -> c.getCollectionId() == 1);
//		allPredicates.add(c -> c.getCollectionId() == 2);
//		
//		return transcriptionRepository.findAll().stream()
//				.filter(allPredicates.stream().reduce(x -> false, Predicate::or)).collect(Collectors.toList());

		// System.out.println("collectionIds---" + collectionIds);

		if (collectionIds != null && !collectionIds.trim().isEmpty()) {

			String[] collections = collectionIds.split(",");

			for (String id : collections) {

				collectionPredicates.add(c -> c.getCollection().getId() == Short.parseShort(id));
			}
		}

		List<Predicate<Transcription>> subseriesPredicates = new ArrayList<Predicate<Transcription>>();

		if (subseriesIds != null && !subseriesIds.trim().isEmpty()) {

			String[] subseries = subseriesIds.split(",");

			for (String id : subseries) {

				subseriesPredicates.add(c -> c.getSubseriesId() == Short.parseShort(id));
			}
		}
		// allPredicates.add(c -> c.getsubseries().get .getSubseriesId() == 44);

		return transcriptionRepository.findAll().stream()
				.filter(collectionPredicates.stream().reduce(x -> false, Predicate::or))
				// .filter(subseriesPredicates.stream().reduce(x -> false, Predicate::or))
				.collect(Collectors.toList());
//		} else {
//			return transcriptionRepository.findAll();
//		}

	}

	@Override
	public List<Transcription> findByCollectinoId(short collectionId) {

		return transcriptionRepository.findAll().stream().filter(t -> t.getCollectionId() == collectionId)
				.collect(Collectors.toList());
	}

//	private static Predicate<Transcription> getCollectionPredicate(String collectionIds) {
//		if (collectionIds != null && !collectionIds.trim().isEmpty()) {
//
//			String[] collections = collectionIds.split(",");
//
//			for (String id : collections) {
//
//				collectionPredicates.add(c -> c.getCollection().getId() == Short.parseShort(id));
//			}
//		}
//	}
}
