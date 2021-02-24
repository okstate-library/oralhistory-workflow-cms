package com.okstatelibrary.oralhistoryworkflowcms.service.Impl;

import java.util.ArrayList;
import java.util.Date;
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
import com.okstatelibrary.oralhistoryworkflowcms.util.DateUtil;
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
	public void delete(int id) {

		Transcription transcription = findById(id).orElse(null);

		transcriptionRepository.delete(transcription);
	}

	@Override
	public Transcription save(Transcription transcription) {
		// TODO Auto-generated method stub
		return transcriptionRepository.save(transcription);
	}

	@Override
	public List<Transcription> findByCollectinoId(short collectionId) {

		return transcriptionRepository.findAll().stream().filter(t -> t.getCollectionId() == collectionId)
				.collect(Collectors.toList());
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

		Stream<Transcription> stream = transcriptionRepository.findAll().stream()
				.filter(u -> u.getSubject().contains(equipmentName));

		return stream == null ? null : stream.collect(Collectors.toList());
	}

	public Double calculateValue(Double initialValue) {
		return initialValue / 1.50;
	}

	@Override
	public List<Transcription> report(String interviewer, String location, String beginDate, String endDate,
			String status, String bornDigital, String converetd) {

		return transcriptionRepository.findAll().stream()
				.filter(getInterviewerPredicate(interviewer).and(getLocationPredicate(location))
						.and(getDatesPredicate(beginDate, endDate)).and(getStatusPredicate(status))
						.and(getBornDigitalPredicate(bornDigital).and(getConveretdPredicate(converetd))))
				.collect(Collectors.toList());

	}

	private static Predicate<Transcription> getDatesPredicate(String beginDate, String endDate) {

		if (!StringUtils.isEmpty(beginDate) && StringUtils.isEmpty(endDate)) {

			Date beign = DateUtil.convertDate(beginDate);

			return u -> u.getInterviewDates().length() == 10 ? !beign.after(DateUtil.convertDate(u.getInterviewDates()))
					: u.getInterviewDates().length() == 21
							? !beign.after(DateUtil.convertDate(u.getInterviewDates().split(",")[0]))
									|| !beign.after(DateUtil.convertDate(u.getInterviewDates().split(",")[1]))
							: u.getInterviewDates().length() == 32
									? !beign.after(DateUtil.convertDate(u.getInterviewDates().split(",")[0]))
											|| !beign.after(DateUtil.convertDate(u.getInterviewDates().split(",")[1]))
											|| !beign.after(DateUtil.convertDate(u.getInterviewDates().split(",")[2]))
									: u.getInterviewDates().length() == 43 ? !beign
											.after(DateUtil.convertDate(u.getInterviewDates().split(",")[0]))
											|| !beign.after(DateUtil.convertDate(u.getInterviewDates().split(",")[1]))
											|| !beign.after(DateUtil.convertDate(u.getInterviewDates().split(",")[2]))
											|| !beign.after(DateUtil.convertDate(u.getInterviewDates().split(",")[3]))
											: u.getId() > 0;

		} else if (StringUtils.isEmpty(beginDate) && !StringUtils.isEmpty(endDate)) {

			Date end = DateUtil.convertDate(endDate);

			return u -> u.getInterviewDates().length() == 10 ? !end.before(DateUtil.convertDate(u.getInterviewDates()))
					: u.getInterviewDates().length() == 21
							? !end.before(DateUtil.convertDate(u.getInterviewDates().split(",")[0]))
									|| !end.before(DateUtil.convertDate(u.getInterviewDates().split(",")[1]))
							: u.getInterviewDates().length() == 32
									? !end.before(DateUtil.convertDate(u.getInterviewDates().split(",")[0]))
											|| !end.before(DateUtil.convertDate(u.getInterviewDates().split(",")[1]))
											|| !end.before(DateUtil.convertDate(u.getInterviewDates().split(",")[2]))
									: u.getInterviewDates().length() == 43 ? !end
											.before(DateUtil.convertDate(u.getInterviewDates().split(",")[0]))
											|| !end.before(DateUtil.convertDate(u.getInterviewDates().split(",")[1]))
											|| !end.before(DateUtil.convertDate(u.getInterviewDates().split(",")[2]))
											|| !end.before(DateUtil.convertDate(u.getInterviewDates().split(",")[3]))
											: u.getId() > 0;

		} else if (!StringUtils.isEmpty(beginDate) && !StringUtils.isEmpty(endDate)) {

			Date beign = DateUtil.convertDate(beginDate);
			Date end = DateUtil.convertDate(endDate);

			return u -> u.getInterviewDates().length() == 10
					? !beign.after(DateUtil.convertDate(u.getInterviewDates()))
							&& !end.before(DateUtil.convertDate(u.getInterviewDates()))
					: u.getInterviewDates().length() == 21
							? (!beign.after(DateUtil.convertDate(u.getInterviewDates().split(",")[0]))
									|| !beign.after(DateUtil.convertDate(u.getInterviewDates().split(",")[1])))
									&& (!end.before(DateUtil.convertDate(u.getInterviewDates().split(",")[0]))
											|| !end.before(DateUtil.convertDate(u.getInterviewDates().split(",")[1])))
							: u.getInterviewDates().length() == 32
									? (!beign.after(DateUtil.convertDate(u.getInterviewDates().split(",")[0]))
											|| !beign.after(DateUtil.convertDate(u.getInterviewDates().split(",")[1]))
											|| !beign.after(DateUtil.convertDate(u.getInterviewDates().split(",")[2])))
											&& (!end.before(DateUtil.convertDate(u.getInterviewDates().split(",")[0]))
													|| !end.before(
															DateUtil.convertDate(u.getInterviewDates().split(",")[1]))
													|| !end.before(
															DateUtil.convertDate(u.getInterviewDates().split(",")[2])))
									: u.getInterviewDates().length() == 43 ? (!beign
											.after(DateUtil.convertDate(u.getInterviewDates().split(",")[0]))
											|| !beign.after(DateUtil.convertDate(u.getInterviewDates().split(",")[1]))
											|| !beign.after(DateUtil.convertDate(u.getInterviewDates().split(",")[2]))
											|| !beign.after(DateUtil.convertDate(u.getInterviewDates().split(",")[3])))
											&& (!end.before(DateUtil.convertDate(u.getInterviewDates().split(",")[0]))
													|| !end.before(
															DateUtil.convertDate(u.getInterviewDates().split(",")[1]))
													|| !end.before(
															DateUtil.convertDate(u.getInterviewDates().split(",")[2]))
													|| !end.before(
															DateUtil.convertDate(u.getInterviewDates().split(",")[3])))
											: u.getId() > 0;

		}
		return u -> u.getId() > 0;
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
	public List<Transcription> findAll(String collectionIds, String subseriesIds, String interviewes, String subjects,
			String darkarchive, String onlineoffline) {

		System.out.println("collectionIds -- " + collectionIds);
		System.out.println("subseriesIds -- " + subseriesIds);
		System.out.println("intervieweroptions -- " + interviewes);
		System.out.println("subjects -- " + subjects);
		System.out.println("darkarchive -- " + darkarchive);
		System.out.println("onlineoffline -- " + onlineoffline);

		List<Predicate<Transcription>> collectionPredicates = null;

		if (collectionIds != null && !collectionIds.trim().isEmpty()) {

			collectionPredicates = new ArrayList<Predicate<Transcription>>();

			String[] collections = collectionIds.split(",");

			for (String id : collections) {

				collectionPredicates.add(c -> c.getCollection().getId() == Short.parseShort(id));
			}
		}

		List<Predicate<Transcription>> subseriesPredicates = null;

		if (subseriesIds != null && !subseriesIds.trim().isEmpty()) {

			subseriesPredicates = new ArrayList<Predicate<Transcription>>();

			String[] subseries = subseriesIds.split(",");

			for (String id : subseries) {

				subseriesPredicates.add(c -> c.getSubseriesId() == Short.parseShort(id));
			}
		}

		List<Predicate<Transcription>> interviewerPredicates = null;

		if (interviewes != null && !interviewes.trim().isEmpty()) {

			interviewerPredicates = new ArrayList<Predicate<Transcription>>();

			String[] interviewers = interviewes.split(",");

			for (String name : interviewers) {

				interviewerPredicates.add(c -> c.getInterviewer().toLowerCase().contains(name.toLowerCase()));
			}
		}

		List<Predicate<Transcription>> subjectPredicates = null;

		if (subjects != null && !subjects.trim().isEmpty()) {

			subjectPredicates = new ArrayList<Predicate<Transcription>>();

			String[] allsubjects = subjects.split(",");

			for (String subject : allsubjects) {

				subjectPredicates.add(c -> c.getSubject().toLowerCase().contains(subject.toLowerCase()));
			}
		}

		List<Predicate<Transcription>> statusPredicates = null;

		if (onlineoffline != null && !onlineoffline.trim().isEmpty()) {

			statusPredicates = new ArrayList<Predicate<Transcription>>();

			if (onlineoffline.contains("online")) {
				statusPredicates.add(c -> c.getIsOnline() == (byte) 1);
			} else if (onlineoffline.contains("offline")) {
				statusPredicates.add(c -> c.getIsOnline() == (byte) 0);
			}

		}

		List<Predicate<Transcription>> darkArchievePredicates = null;

		if (darkarchive != null && !darkarchive.trim().isEmpty()) {

			darkArchievePredicates = new ArrayList<Predicate<Transcription>>();

			if (darkarchive.equals("true")) {
				darkArchievePredicates.add(c -> c.getIsDarkArchive() == (byte) 1);

			}
		}

		return transcriptionRepository.findAll().stream()
				.filter(collectionPredicates != null && collectionPredicates.size() > 0
						? collectionPredicates.stream().reduce(x -> false, Predicate::or)
						: u -> u.getId() > 0)
				.filter(subseriesPredicates != null && subseriesPredicates.size() > 0
						? subseriesPredicates.stream().reduce(x -> false, Predicate::or)
						: u -> u.getId() > 0)
				.filter(interviewerPredicates != null && interviewerPredicates.size() > 0
						? interviewerPredicates.stream().reduce(x -> false, Predicate::or)
						: u -> u.getId() > 0)
				.filter(subjectPredicates != null && subjectPredicates.size() > 0
						? subjectPredicates.stream().reduce(x -> false, Predicate::or)
						: u -> u.getId() > 0)
				.filter(statusPredicates != null && statusPredicates.size() > 0
						? statusPredicates.stream().reduce(x -> false, Predicate::or)
						: u -> u.getId() > 0)
				.filter(darkArchievePredicates != null && darkArchievePredicates.size() > 0
						? darkArchievePredicates.stream().reduce(x -> false, Predicate::or)
						: u -> u.getId() > 0)
				.collect(Collectors.toList());

	}

}
