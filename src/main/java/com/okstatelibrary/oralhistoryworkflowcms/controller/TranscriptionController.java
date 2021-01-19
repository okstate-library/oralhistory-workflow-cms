package com.okstatelibrary.oralhistoryworkflowcms.controller;

import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okstatelibrary.oralhistoryworkflowcms.entity.*;
import com.okstatelibrary.oralhistoryworkflowcms.enums.ErrorType;
import com.okstatelibrary.oralhistoryworkflowcms.enums.OriginalMediumType;
import com.okstatelibrary.oralhistoryworkflowcms.enums.PredefinedUserType;
import com.okstatelibrary.oralhistoryworkflowcms.service.*;
import com.okstatelibrary.oralhistoryworkflowcms.util.DateUtil;
import com.okstatelibrary.oralhistoryworkflowcms.util.StringUtil;

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
	private SubserieService subserieService;

	@Autowired
	private PredefinedUserService predefinedUserService;

	@Autowired
	private EquipmentService equipmentService;

	// @Autowired
	List<Transcription> transcriptionList;

	@Autowired
	private SubjectService subjectService;

	ObjectMapper mapper = new ObjectMapper();

	private static List<TranscriptionQueueOption> queueOptions = new ArrayList<TranscriptionQueueOption>();

	static {
		queueOptions.add(new TranscriptionQueueOption(1, "All", true));
		queueOptions.add(new TranscriptionQueueOption(2, "Priority", false));
		queueOptions.add(new TranscriptionQueueOption(3, "Transcribed", false));
		queueOptions.add(new TranscriptionQueueOption(4, "Audit check", false));
		queueOptions.add(new TranscriptionQueueOption(5, "First edit", false));
		queueOptions.add(new TranscriptionQueueOption(6, "Second edit", false));
		queueOptions.add(new TranscriptionQueueOption(7, "Draft sent", false));
		queueOptions.add(new TranscriptionQueueOption(8, "Corrections", false));
		queueOptions.add(new TranscriptionQueueOption(9, "Final edit", false));
		queueOptions.add(new TranscriptionQueueOption(10, "Sent out", false));
	}

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

		model.addAttribute("transciprtionCount", transcriptionService.findAll(true).size());
		// .filter(u->u.getTranscriptStatus() == 1)
		// .collect(Collectors.toList());

		model.addAttribute("browseCount", transcriptionService.findAll().size());

		return "index";
	}

	@RequestMapping("/browse")
	public String browse(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		model.addAttribute("collections", collectionService.findAll());
		model.addAttribute("subseries", subserieService.findAll());

		model.addAttribute("interviewers",
				predefinedUserService.findAllByType(PredefinedUserType.INTERVIEWER.getCode()));

		model.addAttribute("subjects", subjectService.findAll());

		model.addAttribute("transcriptions", transcriptionService.findAll());

		return "browse";
	}

	@RequestMapping(value = "/browse/data", method = RequestMethod.GET)
	public @ResponseBody List<Transcription> browseData(@RequestParam(required = false) String collectionoptions)
			throws JsonProcessingException {

		if (StringUtil.isNotNullOrEmpty(collectionoptions)) {

			return transcriptionService.findAll(collectionoptions);
		} else {
			return transcriptionService.findAll();
		}

		// return null;
//
//		boolean isPriority = false, isTranscribed = false, isAuditCheck = false, isFirstEdit = false,
//				isSecondEdit = false, isDraftSent = false, isCorrections = false, isFinalEdit = false,
//				isSsentOut = false;
//
//		if (options != null && !options.isBlank() && !options.isEmpty()) {
//
//			isPriority = options.contains("2");
//			isTranscribed = options.contains("3");
//			isAuditCheck = options.contains("4");
//			isFirstEdit = options.contains("5");
//			isSecondEdit = options.contains("6");
//			isDraftSent = options.contains("7");
//			isCorrections = options.contains("8");
//			isFinalEdit = options.contains("9");
//			isSsentOut = options.contains("10");
//		}
//
//		List<Transcription> list = transcriptionService.findAll(false, isPriority, isTranscribed, isAuditCheck,
//				isFirstEdit, isSecondEdit, isDraftSent, isCorrections, isFinalEdit, isSsentOut);

		// s.subList(0, 1);
	}

	@RequestMapping(value = "/transcriptionqueue", method = RequestMethod.GET)
	public String transcriptionqueue(@RequestParam(required = false) String options, HttpServletRequest request,
			Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		model.addAttribute("queueptions", queueOptions);

		return "transcriptionqueue";
	}

	@RequestMapping(value = "/transcriptionqueue/data", method = RequestMethod.GET)
	public @ResponseBody List<Transcription> transcriptionQueueData(@RequestParam(required = false) String options,
			@RequestParam(required = false) String isinitialize) throws JsonProcessingException {

		System.out.println("options" + options);

		System.out.println("isinitialize" + isinitialize);

//		boolean isPriority = false, isTranscribed = false, isAuditCheck = false, isFirstEdit = false,
//				isSecondEdit = false, isDraftSent = false, isCorrections = false, isFinalEdit = false,
//				isSentOut = false;
//
//		if (options != null && !options.isEmpty()) {
//
//			isPriority = options.contains("2");
//			isTranscribed = options.contains("3");
//			isAuditCheck = options.contains("4");
//			isFirstEdit = options.contains("5");
//			isSecondEdit = options.contains("6");
//			isDraftSent = options.contains("7");
//			isCorrections = options.contains("8");
//			isFinalEdit = options.contains("9");
//			isSentOut = options.contains("10");
//		}

		List<Transcription> list = null;

		if (isinitialize != null && !isinitialize.isEmpty() && Boolean.parseBoolean(isinitialize)) {

			System.out.println("optional is null");

			list = transcriptionService.findAll(false);

			this.transcriptionList = list;

		} else {

			System.out.println("optional is not null");

			list = this.transcriptionList.stream()
					.filter(u -> u.getIsPriority() == (byte) (options.contains("2") ? 1 : u.getIsPriority()))
					.filter(u -> options.contains("3") ? StringUtil.isNotNullOrEmpty(u.getTranscriberAssigned()) : true)
					.filter(u -> options.contains("4") ? StringUtil.isNotNullOrEmpty(u.getAuditCheckCompleted()) : true)
					.filter(u -> options.contains("5") ? StringUtil.isNotNullOrEmpty(u.getFirstEditCompleted()) : true)
					.filter(u -> options.contains("6") ? StringUtil.isNotNullOrEmpty(u.getSecondEditCompleted()) : true)
					.filter(u -> options.contains("7") ? u.getDraftSentDate() != null : true)
					.filter(u -> options.contains("8") ? StringUtil.isNotNullOrEmpty(u.getEditWithCorrectionCompleted())
							: true)
					.filter(u -> options.contains("9") ? StringUtil.isNotNullOrEmpty(u.getFinalEditCompleted()) : true)
					.filter(u -> options.contains("10") ? u.getFinalSentDate() != null : true)
					.collect(Collectors.toList());
		}

//		List<Transcription> list = transcriptionService.findAll(false, isPriority, isTranscribed, isAuditCheck,
//				isFirstEdit, isSecondEdit, isDraftSent, isCorrections, isFinalEdit, isSsentOut);

		return list;
	}

	@RequestMapping("/addinterview")
	public String addinterview(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		model.addAttribute("collections", collectionService.findAll());
		model.addAttribute("subseries", subserieService.findAll());

		model.addAttribute("audioEquipments", equipmentService.findAllAudios());
		model.addAttribute("videoEquipments", equipmentService.findAllVideos());

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

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		transcription.setTitle("Oral history interview with" + transcription.getInterviewee());

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

		return "redirect:/addinterview";
	}

	@RequestMapping(value = { "/modifyinterview/{id}" })
	public String modifyinterview(@PathVariable(required = false) String id, Principal principal, Model model) {

		System.out.println("id" + id);

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		// Transcription transcription = null;

		// if (id != null && !id.isEmpty()) {
		Transcription transcription = transcriptionService.findById(Integer.parseInt(id)).orElse(null);

//			model.addAttribute("collectiondetails", collection);
//		}

		// System.out.println("title" + transcription.getTitle());
		// System.out.println("getIsConvertToDigital" +
		// transcription.getIsConvertToDigital());

		// Master data for drop downs
		model.addAttribute("collections", collectionService.findAll());
		model.addAttribute("subseries", subserieService.findAll());
		model.addAttribute("audioEquipments", equipmentService.findAllAudios());
		model.addAttribute("videoEquipments", equipmentService.findAllVideos());

		model.addAttribute("transcribers",
				predefinedUserService.findAllByType(PredefinedUserType.TRANSCRIBER.getCode()));

		model.addAttribute("interviewdetails", transcription);

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

		transcriptionService.save(newTranscription);

		return "redirect:/modifyinterview/" + transcription.getId();
	}

	@RequestMapping(value = "/modifymedia", method = RequestMethod.POST)
	public String modifymedia(@Valid Transcription transcription, BindingResult bindingResult, Model model,
			Principal principal) throws ParseException {

		Transcription newTranscription = transcriptionService.findById(transcription.getId()).orElse(null);

		System.out.println("getConvertToDigitalDate" + transcription.getConvertToDigitalDate());

		// Media
		newTranscription.setIsAudioFormat(transcription.getIsAudioFormat());
		newTranscription.setIsVideoFormat(transcription.getIsVideoFormat());

		// Born digital
		newTranscription.setOriginalMediumType(transcription.getOriginalMediumType());

		if (transcription.getOriginalMediumType() == OriginalMediumType.OTHER.getOriginalMediumTypeCode()) {
			newTranscription.setOriginalMedium(transcription.getOriginalMedium());
		} else {
			newTranscription.setOriginalMedium("");
		}

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

		transcriptionService.save(newTranscription);

		return "redirect:/modifyinterview/" + transcription.getId();
	}

	@RequestMapping(value = "/modifymetadata", method = RequestMethod.POST)
	public String modifymetadata(@Valid Transcription transcription, BindingResult bindingResult, Model model,
			Principal principal) throws ParseException {

		Transcription newTranscription = transcriptionService.findById(transcription.getId()).orElse(null);

		System.out.println("getConvertToDigitalDate" + transcription.getTitle());

		// Title
		newTranscription.setTitle(transcription.getTitle());

		// Interviewee
		newTranscription.setInterviewee(transcription.getInterviewee());

		// InterviewDates
		newTranscription.setInterviewDates(transcription.getInterviewDates());

		// Date digital.
		newTranscription.setConvertToDigitalDate(transcription.getConvertToDigitalDate());

		// Subject
		newTranscription.setSubject(transcription.getSubject());

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

		transcriptionService.save(newTranscription);

		return "redirect:/modifyinterview/" + transcription.getId();
	}

	@RequestMapping(value = "/modifysupplemental", method = RequestMethod.POST)
	public String modifysupplemental(@Valid Transcription transcription, BindingResult bindingResult, Model model,
			Principal principal) throws ParseException {

		Transcription newTranscription = transcriptionService.findById(transcription.getId()).orElse(null);

		// IsOnline
		newTranscription.setIsOnline(transcription.getIsOnline());

		// IsRosetta
		newTranscription.setIsRosetta(transcription.getIsRosetta());

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

		transcriptionService.save(newTranscription);

		return "redirect:/modifyinterview/" + transcription.getId();
	}

	@RequestMapping("/reports")
	public String reports(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		// model.addAttribute("transcriptions", transcriptionService.findAll(false));

		return "reports";
	}

}