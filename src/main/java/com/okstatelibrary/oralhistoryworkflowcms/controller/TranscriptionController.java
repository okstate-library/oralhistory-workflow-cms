package com.okstatelibrary.oralhistoryworkflowcms.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okstatelibrary.oralhistoryworkflowcms.entity.*;
import com.okstatelibrary.oralhistoryworkflowcms.entity.ui.*;
import com.okstatelibrary.oralhistoryworkflowcms.enums.*;
import com.okstatelibrary.oralhistoryworkflowcms.service.*;
import com.okstatelibrary.oralhistoryworkflowcms.util.*;

@Controller
@RequestMapping("")
public class TranscriptionController extends MainController {

	@Autowired
	private UserService userService;

	@Autowired
	private TranscriptionService transcriptionService;

	@Autowired
	private CollectionService collectionService;

	@Autowired
	private MediumService mediumService;

	@Autowired
	private PredefinedUserService predefinedUserService;

	@Autowired
	private EquipmentService equipmentService;

	@Autowired
	private SubserieService subserieService;

	@Autowired
	private SubjectService subjectService;

	// @Autowired
	List<Transcription> transcriptionList;

	ObjectMapper mapper = new ObjectMapper();

	public TranscriptionController() {
		transcriptionList = new ArrayList<Transcription>();
	}

	@GetMapping("/index")
	public String index(Principal principal, Model model) throws ParseException {

		User user = null;

		if (principal != null) {
			user = userService.findByUsername(principal.getName());
			model.addAttribute("transcriptions", transcriptionService.findRecentlyAdded(user.getUserId()));
		}

		model.addAttribute("user", user);

		model.addAttribute("transciprtionCount", transcriptionService.findAll(false).size());

		model.addAttribute("browseCount", transcriptionService.findAll().size());

		return "index";
	}

	@RequestMapping("/validate")
	public String validate(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		this.messageSetup(model);

		return "validate";
	}

//	@RequestMapping("/validatesubjects")
//	public String validatesubjects(Model model, Principal principal) throws ParseException {
//
//		User user = userService.findByUsername(principal.getName());
//		model.addAttribute("user", user);
//
//		this.messageSetup(model);
//
//		return "validate";
//	}

	@RequestMapping("/browse")
	public String browse(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		this.messageSetup(model);

		return "browse";
	}

	@Lazy
	@RequestMapping(value = "/browse/data", method = RequestMethod.GET)
	public @ResponseBody BrowsePageObject browseData(@RequestParam(required = false) String collections,
			@RequestParam(required = false) String subseries, @RequestParam(required = false) String interviewers,
			@RequestParam(required = false) String subjects, @RequestParam(required = false) String onlineoffline,
			@RequestParam(required = false) String darkarchive, Model model) throws JsonProcessingException {

		BrowsePageObject browsePageObject = new BrowsePageObject();

		if (StringUtil.isNotNullOrNotEmpty(collections) || StringUtil.isNotNullOrNotEmpty(subseries)
				|| StringUtil.isNotNullOrNotEmpty(interviewers) || StringUtil.isNotNullOrNotEmpty(subjects)
				|| StringUtil.isNotNullOrNotEmpty(darkarchive) || StringUtil.isNotNullOrNotEmpty(onlineoffline)) {

			System.out.println("selected collections: " + collections);
			System.out.println("selected subseries: " + subseries);
			System.out.println("selected interviewers: " + interviewers);
			System.out.println("selected onlineoffline: " + onlineoffline);
			System.out.println("selected dark archive: " + darkarchive);

			List<Transcription> transcripts = transcriptionService.findAll(collections, subseries, interviewers,
					subjects, darkarchive, onlineoffline);

			browsePageObject.setTranscriptions(transcripts);

			List<CheckBoxObject> selectedCollections = getDistinctCollections(transcripts);

			browsePageObject.setCollections(selectedCollections);

			browsePageObject.setSubseries(getDistinctSubseries(transcripts, selectedCollections.size()));

			browsePageObject.setInterviewers(getDistinctInterviewers(transcripts));

			browsePageObject.setSubjects(getDistinctSubjects(transcripts));

			browsePageObject.setStatusOnline(getStatusOnline(transcripts));

			browsePageObject.setDarkArchieve(getDarkArchieve(transcripts));

		} else {

			browsePageObject.setTranscriptions(transcriptionService.findAll());

			browsePageObject.setCollections(ConvertCollectionToCheckBoxObject(collectionService.findAll()));

			browsePageObject.setSubseries(ConvertSubseriesToCheckBoxObject(subserieService.findAll()));

			browsePageObject.setInterviewers(ConvertInterviewersToCheckBoxObject(
					predefinedUserService.findAllByType(PredefinedUserType.INTERVIEWER.getCode())));

			browsePageObject.setSubjects(ConvertSubjectsToCheckBoxObject(subjectService.findAll()));

			browsePageObject.setStatusOnline(ConvertStatusOnlineToCheckBoxObject());

			browsePageObject.setDarkArchieve(ConvertDarkArchieveToCheckBoxObject());

		}

		return browsePageObject;
	}

	private List<CheckBoxObject> getDarkArchieve(List<Transcription> transcripts) {

		Map<Byte, Long> newTranscripts = transcripts.stream()
				.collect(Collectors.groupingBy(Transcription::getIsDarkArchive, Collectors.counting()));

		List<CheckBoxObject> checkBoxObjectList = new ArrayList<CheckBoxObject>();

		for (Map.Entry<Byte, Long> entry : newTranscripts.entrySet()) {
			Byte darkArchieve = entry.getKey();

			if (darkArchieve == 1) {
				checkBoxObjectList
						.add(new CheckBoxObject(1, "browseDarkArchiveOption", "Dark Archive", entry.getValue()));
			}
		}

		return checkBoxObjectList;
	}

	private List<CheckBoxObject> ConvertDarkArchieveToCheckBoxObject() {
		List<CheckBoxObject> checkBoxObjectList = new ArrayList<CheckBoxObject>();

		checkBoxObjectList.add(new CheckBoxObject(1, "browseDarkArchiveOption", "Dark Archive"));

		return checkBoxObjectList;
	}

	private List<CheckBoxObject> getStatusOnline(List<Transcription> transcripts) {
		Map<Byte, Long> newTranscripts = transcripts.stream()
				.collect(Collectors.groupingBy(Transcription::getIsOnline, Collectors.counting()));

		List<CheckBoxObject> checkBoxObjectList = new ArrayList<CheckBoxObject>();

		for (Map.Entry<Byte, Long> entry : newTranscripts.entrySet()) {
			Byte status = entry.getKey();

			if (status == 0) {
				checkBoxObjectList.add(new CheckBoxObject(1, "browseIsOfflineOption", "No", entry.getValue()));

			} else {
				checkBoxObjectList.add(new CheckBoxObject(1, "browseIsOnlineOption", "Yes", entry.getValue()));
			}

		}

		return checkBoxObjectList;
	}

	private List<CheckBoxObject> ConvertStatusOnlineToCheckBoxObject() {
		List<CheckBoxObject> checkBoxObjectList = new ArrayList<CheckBoxObject>();

		checkBoxObjectList.add(new CheckBoxObject(1, "browseIsOfflineOption", "No"));
		checkBoxObjectList.add(new CheckBoxObject(2, "browseIsOnlineOption", "Yes"));

		return checkBoxObjectList;
	}

	private List<CheckBoxObject> getDistinctCollections(List<Transcription> transcripts) {

		Map<Collection, Long> newTranscripts = transcripts.stream()
				.collect(Collectors.groupingBy(Transcription::getCollection, Collectors.counting()));

		List<CheckBoxObject> checkBoxObjectList = new ArrayList<CheckBoxObject>();

		for (Map.Entry<Collection, Long> entry : newTranscripts.entrySet()) {
			Collection collection = entry.getKey();

			checkBoxObjectList.add(new CheckBoxObject(collection.getId(), collection.getCollectionName(),
					collection.getCollectionName(), entry.getValue()));

		}

		return checkBoxObjectList;
	}

	private List<CheckBoxObject> ConvertCollectionToCheckBoxObject(List<Collection> collections) {
		List<CheckBoxObject> checkBoxObjectList = new ArrayList<CheckBoxObject>();

		for (Collection collection : collections) {
			checkBoxObjectList.add(new CheckBoxObject(collection.getId(), collection.getCollectionName(),
					collection.getCollectionName()));
		}

		return checkBoxObjectList;
	}

	private List<CheckBoxObject> getDistinctSubseries(List<Transcription> transcripts, int collectionSize) {

		Map<Subserie, Long> newTranscripts = transcripts.stream()
				.collect(Collectors.groupingBy(Transcription::getsubseries, Collectors.counting()));

		List<CheckBoxObject> checkBoxObjectList = new ArrayList<CheckBoxObject>();

		int numberOfSubseries = newTranscripts.size();

		for (Map.Entry<Subserie, Long> entry : newTranscripts.entrySet()) {

			Subserie subserie = entry.getKey();

			if (collectionSize == 1 && numberOfSubseries > 1) {
				checkBoxObjectList.add(new CheckBoxObject(subserie.getId(), subserie.getSubseriesName(),
						subserie.getSubseriesName(), entry.getValue()));
			} else {
				if (!subserie.getSubseriesName().toLowerCase().contentEquals("n/a")) {

					checkBoxObjectList.add(new CheckBoxObject(subserie.getId(), subserie.getSubseriesName(),
							subserie.getSubseriesName(), entry.getValue()));
				}
			}

		}

		return checkBoxObjectList;
	}

	private List<CheckBoxObject> ConvertSubseriesToCheckBoxObject(List<Subserie> subseries) {

		List<CheckBoxObject> checkBoxObjectList = new ArrayList<CheckBoxObject>();

		for (Subserie subserie : subseries) {

			if (!subserie.getSubseriesName().toLowerCase().contentEquals("n/a")) {

				checkBoxObjectList.add(
						new CheckBoxObject(subserie.getId(), subserie.getSubseriesName(), subserie.getSubseriesName()));
			}
		}

		return checkBoxObjectList;
	}

	private List<CheckBoxObject> getDistinctInterviewers(List<Transcription> transcripts) {

		List<String> interviewers = new ArrayList<String>();

		for (Transcription transcript : transcripts) {

			if (StringUtil.isNotNullOrNotEmpty(transcript.getInterviewer())) {

				String[] selectedInterviewers = transcript.getInterviewer().split("; ");

				for (String selectedinterviewer : selectedInterviewers) {

					interviewers.add(selectedinterviewer);
				}
			}
		}

		Map<String, Long> newTranscripts = interviewers.stream()
				.collect(Collectors.groupingBy(s -> s, Collectors.counting()));

		List<CheckBoxObject> checkBoxObjectList = new ArrayList<CheckBoxObject>();

		int i = 0;

		for (Map.Entry<String, Long> entry : newTranscripts.entrySet()) {

			checkBoxObjectList.add(new CheckBoxObject(i++, entry.getKey(), entry.getKey(), entry.getValue()));

		}

		Collections.sort(checkBoxObjectList, Comparator.comparing(CheckBoxObject::getName));

		return checkBoxObjectList;
	}

	private List<CheckBoxObject> ConvertInterviewersToCheckBoxObject(List<PredefinedUser> interviewers) {

		List<CheckBoxObject> checkBoxObjectList = new ArrayList<CheckBoxObject>();

		for (PredefinedUser interviewer : interviewers) {
			checkBoxObjectList
					.add(new CheckBoxObject(interviewer.getId(), interviewer.getName(), interviewer.getName()));
		}

		return checkBoxObjectList;
	}

	private List<CheckBoxObject> getDistinctSubjects(List<Transcription> transcripts) {

		List<String> subjects = new ArrayList<String>();

		for (Transcription transcript : transcripts) {

			if (StringUtil.isNotNullOrNotEmpty(transcript.getSubject())) {
				String[] selectedSubjects = transcript.getSubject().split("; ");

				for (String selectedSubject : selectedSubjects) {

					subjects.add(selectedSubject);
				}
			}
		}

		Map<String, Long> newTranscripts = subjects.stream()
				.collect(Collectors.groupingBy(s -> s, Collectors.counting()));

		List<CheckBoxObject> checkBoxObjectList = new ArrayList<CheckBoxObject>();

		int i = 0;

		for (Map.Entry<String, Long> entry : newTranscripts.entrySet()) {

			checkBoxObjectList.add(new CheckBoxObject(++i, entry.getKey(), entry.getKey(), entry.getValue()));

		}

		Collections.sort(checkBoxObjectList, Comparator.comparing(CheckBoxObject::getName));
		
		return checkBoxObjectList;
	}

	private List<CheckBoxObject> ConvertSubjectsToCheckBoxObject(List<Subject> subjects) {

		List<CheckBoxObject> checkBoxObjectList = new ArrayList<CheckBoxObject>();

		for (Subject subject : subjects) {

			checkBoxObjectList
					.add(new CheckBoxObject(subject.getId(), subject.getSubjectName(), subject.getSubjectName()));

		}

		return checkBoxObjectList;
	}

	// Utility function
	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	@RequestMapping(value = "/transcriptionqueue", method = RequestMethod.GET)
	public String transcriptionqueue(@RequestParam(required = false) String options, HttpServletRequest request,
			Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		// model.addAttribute("queueptions", queueOptions);

		return "transcriptionqueue";
	}

	@RequestMapping(value = "/transcriptionqueue/data", method = RequestMethod.GET)
	public @ResponseBody List<Transcription> transcriptionQueueData() throws JsonProcessingException {

		return transcriptionService.findAll(false);
	}

	@RequestMapping("/addinterview")
	public String addinterview(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		model.addAttribute("collections", collectionService.findAll());
		model.addAttribute("subseries", subserieService.findAll());

		model.addAttribute("audioEquipments", equipmentService.findAllByType(EquipmentType.AUDIO.getCode()));
		model.addAttribute("videoEquipments", equipmentService.findAllByType(EquipmentType.VIDEO.getCode()));

		model.addAttribute("transcribers",
				predefinedUserService.findAllByType(PredefinedUserType.TRANSCRIBER.getCode()));

		model.addAttribute("interviewers",
				predefinedUserService.findAllByType(PredefinedUserType.INTERVIEWER.getCode()));

		model.addAttribute("transcription", new Transcription());

		this.messageSetup(model);

		return "addinterview";
	}

	@RequestMapping(value = "/addinterview", method = RequestMethod.POST)
	public String addinterviewPost(@Valid Transcription transcription, BindingResult bindingResult, Model model,
			Principal principal) throws ParseException {

		// check for errors
		if (bindingResult.hasErrors()) {
			return "addinterview";
		}

		if (transcriptionService.isProjectCodeExists(transcription.getProjectCode())) {
			this.errorType = ErrorType.ERROR;
			this.message = "Project Code exists!";

		} else {

			User user = userService.findByUsername(principal.getName());
			model.addAttribute("user", user);

			transcription.setTitle("Oral history interview with " + transcription.getInterviewee());

			//
//			String interviewers = transcription.getInterviewer().replaceAll(", ", "/");
//			interviewers = interviewers.replaceAll(",", ";");
//			interviewers = interviewers.replaceAll("/", ", ");

			transcription.setInterviewer(setDelimeterdInterviewers(transcription.getInterviewer()));

			transcription.setInterviewDates(transcription.getInterviewDates());

			transcription.setCreatedDate(DateUtil.getTodaySqlDate());
			transcription.setUpdatedDate(DateUtil.getTodaySqlDate());

			transcription.setCreatedBy(Integer.parseInt(user.getUserId().toString()));
			transcription.setUpdatedBy(Integer.parseInt(user.getUserId().toString()));

			Transcription trans = transcriptionService.save(transcription);

			if (trans != null && trans.getId() > 0) {

				this.errorType = ErrorType.SUCESS;
				this.message = "Interview details added successfully!";

			} else {
				this.errorType = ErrorType.ERROR;
				this.message = "Failed to add interview!";
			}
		}

		return "redirect:/addinterview";
	}

	@RequestMapping(value = "/validateprojectcode", method = RequestMethod.POST)
	public @ResponseBody boolean validateProjectCode(@RequestParam(value = "code") String code) {

		return transcriptionService.isProjectCodeExists(code);

	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String deleteinterview(@Valid Transcription transcription, BindingResult bindingResult, Model model,
			Principal principal) throws ParseException {

		transcriptionService.delete(transcription.getId());

		System.out.print(transcription.getId());

		this.errorType = ErrorType.SUCESS;
		this.message = "Interview details deleted successfully!";

		return "redirect:/browse";
	}

	@RequestMapping(value = { "/modifyinterview/{id}" })
	public String modifyinterview(@PathVariable(required = false) String id, Principal principal, Model model) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		Transcription transcription = transcriptionService.findById(Integer.parseInt(id)).orElse(null);

		if (transcription != null) {
			// Master data for drop downs
			model.addAttribute("collections", collectionService.findAll());
			model.addAttribute("subseries", subserieService.findAll());
			model.addAttribute("audioEquipments", equipmentService.findAllByType(EquipmentType.AUDIO.getCode()));
			model.addAttribute("videoEquipments", equipmentService.findAllByType(EquipmentType.VIDEO.getCode()));

			model.addAttribute("transcribers",
					predefinedUserService.findAllByType(PredefinedUserType.TRANSCRIBER.getCode()));

			model.addAttribute("interviewdetails", transcription);
			model.addAttribute("mediums", mediumService.findAll());
			model.addAttribute("interviewers",
					predefinedUserService.findAllByType(PredefinedUserType.INTERVIEWER.getCode()));

			model.addAttribute("subjects", subjectService.findAll());

		} else {
			this.errorType = ErrorType.ERROR;
			this.message = "Relevent interview record cannot find in the system. Please refresh the 'Browse' or 'Transcription queue' page and look for the record.";
		}

		this.messageSetup(model);

		return "modifyinterview";
	}

	@RequestMapping(value = "/modifytranscript", method = RequestMethod.POST)
	public String modifytranscript(@Valid Transcription transcription, BindingResult bindingResult, Model model,
			Principal principal) throws ParseException {

		Transcription newTranscription = transcriptionService.findById(transcription.getId()).orElse(null);

		// Title
		newTranscription.setTranscriptStatus(transcription.getTranscriptStatus());

		// Transcriber Assigned
		newTranscription.setTranscriberAssigned(transcription.getTranscriberAssigned());

		// AuditCheckCompleted
		newTranscription.setAuditCheckCompleted(transcription.getAuditCheckCompleted());

		// FirstEditCompleted
		newTranscription.setFirstEditCompleted(transcription.getFirstEditCompleted());

		// SecondEditCompleted
		newTranscription.setSecondEditCompleted(transcription.getSecondEditCompleted());

		// ThirdEditCompleted
		newTranscription.setThirdEditCompleted(transcription.getThirdEditCompleted());

		// DraftSentDate
		newTranscription.setDraftSentDate(transcription.getDraftSentDate());

		// EditWithCorrectionCompleted
		newTranscription.setEditWithCorrectionCompleted(transcription.getEditWithCorrectionCompleted());

		// FinalEditCompleted
		newTranscription.setFinalEditCompleted(transcription.getFinalEditCompleted());

		// SentOut
		newTranscription.setSentOut(transcription.getSentOut());

		// FinalSentDate
		newTranscription.setFinalSentDate(transcription.getFinalSentDate());

		// MetadataDraft
		newTranscription.setMetadataDraft(transcription.getMetadataDraft());

		// IsPriority
		newTranscription.setIsPriority(transcription.getIsPriority());

		// ReasonForPriority
		newTranscription.setReasonForPriority(transcription.getReasonForPriority());

		// TranscriptLocation
		newTranscription.setTranscriptLocation(transcription.getTranscriptLocation());

		// TranscriptNote
		newTranscription.setTranscriptNote(transcription.getTranscriptNote());

		// InitialNote
		newTranscription.setInitialNote(transcription.getInitialNote());

		return submitTranscription(newTranscription);
	}

	@RequestMapping(value = "/modifymedia", method = RequestMethod.POST)
	public String modifymedia(@Valid Transcription transcription, BindingResult bindingResult, Model model,
			Principal principal) throws ParseException {

		Transcription newTranscription = transcriptionService.findById(transcription.getId()).orElse(null);

		// Media
		newTranscription.setIsAudioFormat(transcription.getIsAudioFormat());
		newTranscription.setIsVideoFormat(transcription.getIsVideoFormat());

		// Born digital
		newTranscription.setIsBornDigital(transcription.getIsBornDigital());

		// Original medium
		newTranscription.setOriginalMedium(transcription.getOriginalMedium());

		// Converted to digital
		newTranscription.setIsConvertToDigital(transcription.getIsConvertToDigital());

		if (transcription.getIsConvertToDigital() == 0) {

			newTranscription.setConvertToDigitalDate("");

		} else {
			newTranscription.setConvertToDigitalDate(transcription.getConvertToDigitalDate());
		}

		// Access media status
		newTranscription.setIsAccessMediaStatus(transcription.getIsAccessMediaStatus());

		// Specification and file locations
		newTranscription.setTechnicalSpecification(transcription.getTechnicalSpecification());
		newTranscription.setMasterFileLocation(transcription.getMasterFileLocation());
		newTranscription.setAccessFileLocation(transcription.getAccessFileLocation());

		return submitTranscription(newTranscription);
	}

	@RequestMapping(value = "/modifymetadata", method = RequestMethod.POST)
	public String modifymetadata(@Valid Transcription transcription, BindingResult bindingResult, Model model,
			Principal principal) throws ParseException {

		Transcription newTranscription = transcriptionService.findById(transcription.getId()).orElse(null);

		// Title
		newTranscription.setTitle(transcription.getTitle());

		// Interviewee
		newTranscription.setInterviewee(transcription.getInterviewee());

		// Interviewee
		newTranscription.setInterviewer(setDelimeterdInterviewers(transcription.getInterviewer()));

		// InterviewDates
		newTranscription.setInterviewDates(transcription.getInterviewDates());

		// Date digital.
		newTranscription.setConvertToDigitalDate(transcription.getConvertToDigitalDate());

		// Subject
		newTranscription.setSubject(StringUtil.isNotNullOrNotEmpty(transcription.getSubject())
				? transcription.getSubject().replaceAll(",", "; ")
				: "");

		// Keywords
		newTranscription.setKeywords(transcription.getKeywords());

		// Description
		newTranscription.setDescription(transcription.getDescription());

		// ScopeAndContents
		newTranscription.setScopeAndContents(transcription.getScopeAndContents());

		// Rights
		newTranscription.setRights(transcription.getRights());

		// Publisher
		newTranscription.setPublisher(transcription.getPublisher());

		// CoverageSpatial
		newTranscription.setCoverageSpatial(transcription.getCoverageSpatial());

		// ProjectCode
		newTranscription.setProjectCode(transcription.getProjectCode());

		// Identifier
		newTranscription.setIdentifier(transcription.getIdentifier());

		// CoverageTemporal
		newTranscription.setCoverageTemporal(transcription.getCoverageTemporal());

		// Format
		newTranscription.setFormat(transcription.getFormat());

		// Language
		newTranscription.setLanguage(transcription.getLanguage());

		// Type
		newTranscription.setType(transcription.getType());

		// Transcript
		newTranscription.setTranscript(transcription.getTranscript());

		// RelationIsPartOf
		newTranscription.setRelationIsPartOf(transcription.getRelationIsPartOf());

		// FileName
		newTranscription.setFileName(transcription.getFileName());

		return submitTranscription(newTranscription);
	}

	@RequestMapping(value = "/modifymiscellaneous", method = RequestMethod.POST)
	public String modifymiscellaneous(@Valid Transcription transcription, BindingResult bindingResult, Model model,
			Principal principal) throws ParseException {

		Transcription newTranscription = transcriptionService.findById(transcription.getId()).orElse(null);

		// IsOnline
		newTranscription.setIsOnline(transcription.getIsOnline());

		// IsRosetta
		newTranscription.setIsRosetta(transcription.getIsRosetta());

		// IsRosetta Form
		newTranscription.setIsRosettaForm(transcription.getIsRosettaForm());

		// Release Form
		newTranscription.setReleaseForm(transcription.getReleaseForm());

		// Is Restriction
		newTranscription.setIsRestriction(transcription.getIsRestriction());

		// Restriction Note
		newTranscription.setRestrictionNote(transcription.getRestrictionNote());

		// Is Dark Archive
		newTranscription.setIsDarkArchive(transcription.getIsDarkArchive());

		// Dark Archive Note
		newTranscription.setDarkArchiveNote(transcription.getDarkArchiveNote());

		// Recording format
		newTranscription.setIsAudioFormat(transcription.getIsAudioFormat());
		newTranscription.setIsVideoFormat(transcription.getIsVideoFormat());

		// Equipment Number
		newTranscription.setEquipmentNumber(transcription.getEquipmentNumber());

		// Audio and video equipment used
		newTranscription.setAudioEquipmentUsed(transcription.getAudioEquipmentUsed());
		newTranscription.setVideoEquipmentUsed(transcription.getVideoEquipmentUsed());

		// TranscriptLocation
		newTranscription.setTranscriptLocation(transcription.getTranscriptLocation());

		// Interviewer Description
		newTranscription.setInterviewerDescription(transcription.getInterviewerDescription());

		// Interviewer Keywords
		newTranscription.setInterviewerKeywords(transcription.getInterviewerKeywords());

		// Interviewer Subjects
		newTranscription.setInterviewerSubjects(transcription.getInterviewerSubjects());

		// Interviewer Note
		newTranscription.setInterviewerNote(transcription.getInterviewerNote());

		// General Note
		newTranscription.setGeneralNote(transcription.getGeneralNote());

		return submitTranscription(newTranscription);
	}

	private String submitTranscription(Transcription newTranscription) {

		this.errorType = ErrorType.SUCESS;
		this.message = "Interview details added successfully!";

		transcriptionService.save(newTranscription);

		return "redirect:/modifyinterview/" + newTranscription.getId();

	}

	@RequestMapping("/reports")
	public String reports(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		model.addAttribute("interviewers",
				predefinedUserService.findAllByType(PredefinedUserType.INTERVIEWER.getCode()));

		return "reports";
	}

	@RequestMapping(value = "/reports", method = RequestMethod.POST)
	@ResponseBody
	public String reportPost(@RequestBody Report report, HttpServletRequest request) {

		HttpSession session = request.getSession();
		session.setAttribute("reportobj", report);

		List<Transcription> transcriptions = getTranscriptions(report);

		return "" + transcriptions.size();
	}

	@PostMapping("/downloadreports")
	public ResponseEntity<Resource> downloadreports(HttpServletRequest request) throws IOException {

		HttpSession session = request.getSession();
		Report report = (Report) session.getAttribute("reportobj");

		return downloadTranscriptions(getTranscriptions(report));
	}

	private List<Transcription> getTranscriptions(Report report) {
		return transcriptionService.report(report.getInterviewer(), report.getLocation(), report.getBeginDate(),
				report.getEndDate(), report.getStatus(), report.getBornDigital(), report.getConverted());
	}

	// ---------------------------------------------------------------------------------------
	// region Find and Replace Implementation
	// ---------------------------------------------------------------------------------------

	@RequestMapping("findreplace")
	public String findreplace(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		this.messageSetup(model);

		return "findreplace";
	}

	@RequestMapping(value = "findreplace", method = RequestMethod.POST)
	public String findreplacePost(@RequestParam(required = true) String option,
			@RequestParam(required = true) String find, @RequestParam(required = true) String replace, Model model,
			Principal principal) throws ParseException {

		System.out.println("option-" + option);
		System.out.println("find-" + find);
		System.out.println("replace- " + replace);

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		int recordCount = transcriptionService.findreplace(FindReplaceOption.values()[Byte.parseByte(option) - 1], find,
				replace);

		if (recordCount > 0) {

			this.errorType = ErrorType.SUCESS;
			this.message = recordCount + " record(s) updated successfully!";

		} else {
			this.errorType = ErrorType.ERROR;
			this.message = "No records found.!";
		}

		return "redirect:/findreplace";
	}

	// ---------------------------------------------------------------------------------------
	// endregion
	// ---------------------------------------------------------------------------------------

	// ---------------------------------------------------------------------------------------
	// region Export Implementation
	// ---------------------------------------------------------------------------------------

	@RequestMapping("export")
	public String export(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		return "export";
	}

	@PostMapping("/transactions/export")
	public ResponseEntity<Resource> export(Model model, HttpServletResponse response) throws IOException {

		List<Transcription> transcriptions = new ArrayList<Transcription>();

		transcriptions.add(transcriptionService.findById(1).orElse(null));

		return downloadTranscriptions(transcriptions);
	}

	// ---------------------------------------------------------------------------------------
	// endregion
	// ---------------------------------------------------------------------------------------

	@RequestMapping("/import")
	public String importdata(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		this.messageSetup(model);

		return "import";
	}

	@RequestMapping(value = "download", method = RequestMethod.POST)
	@ResponseBody
	public FileSystemResource downloadFile() {
		return new FileSystemResource(new File("cms_sample.xlsx"));
	}

	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes, Model model,
			Principal principal) {

		String UPLOAD_DIR = "uploads";

		// check if file is empty
		if (file.isEmpty()) {
			this.errorType = ErrorType.ERROR;
			this.message = "Please select a file to upload!";

			return "import";
		}

		// normalize the file path
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		// save the file on the local file system
		try {
			Path path = Paths.get(UPLOAD_DIR + fileName);

			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			List<Transcription> transcriptions = ExcelService.getTranscriptionFromExcel(path.toString());

			User user = userService.findByUsername(principal.getName());
			model.addAttribute("user", user);

			if (transcriptions != null && transcriptions.size() > 1) {
				for (Transcription transcription : transcriptions) {

					// Setup collection using collection name
					Collection collection = collectionService.findByName(transcription.getCollectionName());

					if (collection != null) {
						transcription.setCollectionId(collection.getId());
					} else {
						transcription.setCollectionId((short) 1);
					}

					// Setup Subseries using suibsreis name
					Subserie subserie = subserieService.findByName(collection.getId(),
							transcription.getSubseriesName());

					if (subserie != null) {
						transcription.setSubseriesId(subserie.getId());
					} else {
						transcription.setSubseriesId((short) 1);
					}

					transcription.setCreatedDate(DateUtil.getTodaySqlDate());
					transcription.setUpdatedDate(DateUtil.getTodaySqlDate());

					transcription.setCreatedBy(Integer.parseInt(user.getUserId().toString()));
					transcription.setUpdatedBy(Integer.parseInt(user.getUserId().toString()));

					transcriptionService.save(transcription);
				}
			}
		} catch (

		IOException e) {
			e.printStackTrace();
		}

		this.errorType = ErrorType.SUCESS;
		this.message = "You successfully uploaded " + fileName + '!';

		this.messageSetup(model);

		return "import";
	}

	private String setDelimeterdInterviewers(String interviewers) {

		interviewers = interviewers.replaceAll(", ", "/");
		interviewers = interviewers.replaceAll(",", ";");
		interviewers = interviewers.replaceAll("/", ", ");

		return interviewers;
	}

	private ResponseEntity<Resource> downloadTranscriptions(List<Transcription> transcriptions) {
		String filename = "transcriptions.xlsx";

		InputStreamResource file = new InputStreamResource(ExcelService.transcriptionToExcel(transcriptions));

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}

}