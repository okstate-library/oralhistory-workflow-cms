package com.okstatelibrary.oralhistoryworkflowcms.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Transcription;
import com.okstatelibrary.oralhistoryworkflowcms.enums.FindReplaceOption;
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
				.filter(u -> u.getSentOut()== (byte) (isTranscripted ? 1 : 0)).collect(Collectors.toList());
	}

	@Override
	public boolean isProjectCodeExists(String code) {

		List<Transcription> list = this.findAll().stream()
				.filter(u -> u.getProjectCode().toLowerCase().equals(code.toLowerCase())).collect(Collectors.toList());

		return list != null && list.size() > 0 ? true : false;
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
	public List<Transcription> findBySeriesId(int seriesId) {

		return transcriptionRepository.findAll().stream().filter(u -> u.getSeriesId() == seriesId)
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
	public boolean isRecordsExistsBySuject(String subjectName) {

		List<Transcription> list = this.findAll().stream()
				.filter(u -> StringUtil.isNotNullOrNotEmpty(u.getSubject()) ? u.getSubject().contains(subjectName)
						: false)
				.collect(Collectors.toList());

		return list != null && list.size() > 0 ? true : false;
		
	}

	@Override
	public boolean isRecordsExistsByEquipmentName(String equipmentName) {

		List<Transcription> audioList = this.findAll().stream()
				.filter(u -> StringUtil.isNotNullOrNotEmpty(u.getAudioEquipmentUsed())
						? u.getAudioEquipmentUsed().contains(equipmentName)
						: false)
				.collect(Collectors.toList());

		List<Transcription> videoList = this.findAll().stream()
				.filter(u -> StringUtil.isNotNullOrNotEmpty(u.getVideoEquipmentUsed())
						? u.getVideoEquipmentUsed().contains(equipmentName)
						: false)
				.collect(Collectors.toList());

		return (audioList != null && audioList.size() > 0) || (videoList != null && videoList.size() > 0) ? true
				: false;
	}

	@Override
	public boolean isRecordsExistsByOriginalMedium(String medium) {

		List<Transcription> list = this.findAll().stream()
				.filter(u -> StringUtil.isNotNullOrNotEmpty(u.getOriginalMedium()) ? u.getOriginalMedium().contains(medium)
						: false)
				.collect(Collectors.toList());

		return list != null && list.size() > 0 ? true : false;
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

		String delimeter = ";";

		if (!StringUtils.isEmpty(beginDate) && StringUtils.isEmpty(endDate)) {

			Date beign = DateUtil.convertDate(beginDate);

			return u -> u.getInterviewDates().length() == 10 ? !beign.after(DateUtil.convertDate(u.getInterviewDates()))
					: u.getInterviewDates().length() == 21
							? !beign.after(DateUtil.convertDate(u.getInterviewDates().split(delimeter)[0]))
									|| !beign.after(DateUtil.convertDate(u.getInterviewDates().split(delimeter)[1]))
							: u.getInterviewDates().length() == 32
									? !beign.after(DateUtil.convertDate(u.getInterviewDates().split(delimeter)[0]))
											|| !beign.after(
													DateUtil.convertDate(u.getInterviewDates().split(delimeter)[1]))
											|| !beign
													.after(DateUtil
															.convertDate(u.getInterviewDates().split(delimeter)[2]))
									: u.getInterviewDates().length() == 43 ? !beign
											.after(DateUtil.convertDate(u.getInterviewDates().split(delimeter)[0]))
											|| !beign.after(
													DateUtil.convertDate(u.getInterviewDates().split(delimeter)[1]))
											|| !beign.after(
													DateUtil.convertDate(u.getInterviewDates().split(delimeter)[2]))
											|| !beign.after(
													DateUtil.convertDate(u.getInterviewDates().split(delimeter)[3]))
											: u.getId() > 0;

		} else if (StringUtils.isEmpty(beginDate) && !StringUtils.isEmpty(endDate)) {

			Date end = DateUtil.convertDate(endDate);

			return u -> u.getInterviewDates().length() == 10 ? !end.before(DateUtil.convertDate(u.getInterviewDates()))
					: u.getInterviewDates().length() == 21
							? !end.before(DateUtil.convertDate(u.getInterviewDates().split(delimeter)[0]))
									|| !end.before(DateUtil.convertDate(u.getInterviewDates().split(delimeter)[1]))
							: u.getInterviewDates().length() == 32
									? !end.before(DateUtil.convertDate(u.getInterviewDates().split(delimeter)[0]))
											|| !end.before(
													DateUtil.convertDate(u.getInterviewDates().split(delimeter)[1]))
											|| !end.before(
													DateUtil.convertDate(u.getInterviewDates().split(delimeter)[2]))
									: u.getInterviewDates().length() == 43 ? !end
											.before(DateUtil.convertDate(u.getInterviewDates().split(delimeter)[0]))
											|| !end.before(
													DateUtil.convertDate(u.getInterviewDates().split(delimeter)[1]))
											|| !end.before(
													DateUtil.convertDate(u.getInterviewDates().split(delimeter)[2]))
											|| !end.before(
													DateUtil.convertDate(u.getInterviewDates().split(delimeter)[3]))
											: u.getId() > 0;

		} else if (!StringUtils.isEmpty(beginDate) && !StringUtils.isEmpty(endDate)) {

			Date beign = DateUtil.convertDate(beginDate);
			Date end = DateUtil.convertDate(endDate);

			System.out.println(beign);
			System.out.println(end);

			return u -> u.getInterviewDates().length() == 10
					? !beign.after(DateUtil.convertDate(u.getInterviewDates()))
							&& !end.before(DateUtil.convertDate(u.getInterviewDates()))
					: u.getInterviewDates().length() == 21
							? (!beign.after(DateUtil.convertDate(u.getInterviewDates().split(delimeter)[0]))
									|| !beign.after(DateUtil.convertDate(u.getInterviewDates().split(delimeter)[1])))
									&& (!end.before(DateUtil.convertDate(u.getInterviewDates().split(delimeter)[0]))
											|| !end.before(
													DateUtil.convertDate(u.getInterviewDates().split(delimeter)[1])))
							: u.getInterviewDates().length() == 32 ? (!beign
									.after(DateUtil.convertDate(u.getInterviewDates().split(delimeter)[0]))
									|| !beign.after(DateUtil.convertDate(u.getInterviewDates().split(delimeter)[1]))
									|| !beign.after(DateUtil.convertDate(u.getInterviewDates().split(delimeter)[2])))
									&& (!end.before(DateUtil.convertDate(u.getInterviewDates().split(delimeter)[0]))
											|| !end.before(
													DateUtil.convertDate(u.getInterviewDates().split(delimeter)[1]))
											|| !end.before(
													DateUtil.convertDate(u.getInterviewDates().split(delimeter)[2])))
									: u.getInterviewDates().length() == 43 ? (!beign
											.after(DateUtil.convertDate(u.getInterviewDates().split(delimeter)[0]))
											|| !beign.after(
													DateUtil.convertDate(u.getInterviewDates().split(delimeter)[1]))
											|| !beign.after(
													DateUtil.convertDate(u.getInterviewDates().split(delimeter)[2]))
											|| !beign.after(
													DateUtil.convertDate(u.getInterviewDates().split(delimeter)[3])))
											&& (!end.before(
													DateUtil.convertDate(u.getInterviewDates().split(delimeter)[0]))
													|| !end.before(DateUtil
															.convertDate(u.getInterviewDates().split(delimeter)[1]))
													|| !end.before(DateUtil
															.convertDate(u.getInterviewDates().split(delimeter)[2]))
													|| !end.before(DateUtil
															.convertDate(u.getInterviewDates().split(delimeter)[3])))
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
	public List<Transcription> findAll(String collectionIds, String seriesIds, String interviewes, String subjects,
			String darkarchive, String onlineoffline) {

		List<Predicate<Transcription>> collectionPredicates = null;

		if (collectionIds != null && !collectionIds.trim().isEmpty()) {

			collectionPredicates = new ArrayList<Predicate<Transcription>>();

			String[] collections = collectionIds.split(",");

			for (String id : collections) {

				collectionPredicates.add(c -> c.getCollection().getId() == Short.parseShort(id));
			}
		}

		List<Predicate<Transcription>> seriesPredicates = null;

		if (seriesIds != null && !seriesIds.trim().isEmpty()) {

			seriesPredicates = new ArrayList<Predicate<Transcription>>();

			String[] series = seriesIds.split(",");

			for (String id : series) {

				seriesPredicates.add(c -> c.getSeriesId() == Short.parseShort(id));
			}
		}

		List<Predicate<Transcription>> interviewerPredicates = null;

		if (interviewes != null && !interviewes.trim().isEmpty()) {

			interviewerPredicates = new ArrayList<Predicate<Transcription>>();

			String[] interviewers = interviewes.split(";");

			for (String name : interviewers) {

				interviewerPredicates.add(c -> c.getInterviewer().toLowerCase().contains(name.toLowerCase()));
			}
		}

		List<Predicate<Transcription>> subjectPredicates = null;

		if (subjects != null && !subjects.trim().isEmpty()) {

			subjectPredicates = new ArrayList<Predicate<Transcription>>();

			String[] allsubjects = subjects.split(",");

			for (String subject : allsubjects) {

				subjectPredicates.add(c -> c.getSubject() != null ?  c.getSubject().toLowerCase().contains(subject.toLowerCase()) : true);
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
				.filter(seriesPredicates != null && seriesPredicates.size() > 0
						? seriesPredicates.stream().reduce(x -> false, Predicate::or)
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

	@Override
	public int findreplace(FindReplaceOption option, String find, String replace) {

		int recordCount = 0;

		for (Transcription transcription : findAll()) {

			switch (option) {
			case All:

				boolean isFoundReplace = false;

				if (StringUtil.isNotNullOrNotEmpty(transcription.getTitle()) && transcription.getTitle().contains(find)) {
					transcription.setTitle(transcription.getTitle().replace(find, replace));
					isFoundReplace = true;
				}

				if (StringUtil.isNotNullOrNotEmpty(transcription.getDescription())
						&& transcription.getDescription().contains(find)) {
					transcription.setDescription(transcription.getDescription().replace(find, replace));

					isFoundReplace = true;
				}

				if (StringUtil.isNotNullOrNotEmpty(transcription.getKeywords())
						&& transcription.getKeywords().contains(find)) {
					transcription.setKeywords(transcription.getKeywords().replace(find, replace));

					isFoundReplace = true;
				}

				if (StringUtil.isNotNullOrNotEmpty(transcription.getSubject())
						&& transcription.getSubject().contains(find)) {
					transcription.setSubject(transcription.getSubject().replace(find, replace));

					isFoundReplace = true;
				}

				if (isFoundReplace) {
					transcriptionRepository.save(transcription);
					recordCount++;
				}

				// code block
				break;
			case Title:

				if (StringUtil.isNotNullOrNotEmpty(transcription.getTitle()) && transcription.getTitle().contains(find)) {
					transcription.setTitle(transcription.getTitle().replace(find, replace));

					transcriptionRepository.save(transcription);
					recordCount++;
				}

				break;
			case Description:
				if (StringUtil.isNotNullOrNotEmpty(transcription.getDescription())
						&& transcription.getDescription().contains(find)) {
					transcription.setDescription(transcription.getDescription().replace(find, replace));

					transcriptionRepository.save(transcription);
					recordCount++;
				}
				break;
			case Keywords:
				if (StringUtil.isNotNullOrNotEmpty(transcription.getKeywords())
						&& transcription.getKeywords().contains(find)) {
					transcription.setKeywords(transcription.getKeywords().replace(find, replace));

					transcriptionRepository.save(transcription);
					recordCount++;
				}
				break;
			case Subject:
				if (StringUtil.isNotNullOrNotEmpty(transcription.getSubject())
						&& transcription.getSubject().contains(find)) {
					transcription.setSubject(transcription.getSubject().replace(find, replace));

					transcriptionRepository.save(transcription);
					recordCount++;
				}
				break;
			default:
				// code block
			}

		}

		return recordCount;

	}

}
