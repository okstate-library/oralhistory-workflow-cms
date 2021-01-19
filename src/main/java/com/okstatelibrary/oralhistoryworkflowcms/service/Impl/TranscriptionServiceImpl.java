package com.okstatelibrary.oralhistoryworkflowcms.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public List<Transcription> findAll(String collectionIds) {

		// return transcriptionRepository.findAll().stream().filter(u ->
		// u.getCollectionId() == 21)
		// .collect(Collectors.toList());

		List<Predicate<Transcription>> allPredicates = new ArrayList<Predicate<Transcription>>();
//
//		allPredicates.add(c -> c.getCollectionId() == 1);
//		allPredicates.add(c -> c.getCollectionId() == 2);
//		
//		return transcriptionRepository.findAll().stream()
//				.filter(allPredicates.stream().reduce(x -> false, Predicate::or)).collect(Collectors.toList());

		System.out.println("collectionIds---" + collectionIds);

		String[] collections = collectionIds.split(",");

		System.out.println("collections.length" + collections.length);

		// if (collections.length > 1) {
		for (String id : collections) {

			System.out.println("id" + id);

			// .getCollectionId() //c.getCollection().getId()
			allPredicates.add(c -> c.getCollection().getId() == Short.parseShort(id));
		}

		//allPredicates.add(c -> c.getsubseries().get .getSubseriesId() == 44);

		return transcriptionRepository.findAll().stream()
				.filter(allPredicates.stream().reduce(x -> false, Predicate::or)).collect(Collectors.toList());
//		} else {
//			return transcriptionRepository.findAll();
//		}

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

}