package com.okstatelibrary.oralhistoryworkflowcms.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
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
import com.okstatelibrary.oralhistoryworkflowcms.enums.EquipmentType;
import com.okstatelibrary.oralhistoryworkflowcms.enums.ErrorType;
import com.okstatelibrary.oralhistoryworkflowcms.enums.OriginalMediumType;
import com.okstatelibrary.oralhistoryworkflowcms.enums.PredefinedUserType;
import com.okstatelibrary.oralhistoryworkflowcms.service.*;
import com.okstatelibrary.oralhistoryworkflowcms.util.DateUtil;
import com.okstatelibrary.oralhistoryworkflowcms.util.ExcelService;
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

		List<Transcription> transcriptions = null;

		if (StringUtil.isNotNullOrEmpty(collectionoptions)) {

			transcriptions = transcriptionService.findAll(collectionoptions);
		} else {
			transcriptions = transcriptionService.findAll();
		}

		return transcriptions;

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

		List<Transcription> list = null;

		System.out.println("options -- " + options);
		
		if (isinitialize != null && !isinitialize.isEmpty() && Boolean.parseBoolean(isinitialize)) {

			list = transcriptionService.findAll(false);

			this.transcriptionList = list;

		} else {

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

		return list;
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

		Transcription transcription = transcriptionService.findById(Integer.parseInt(id)).orElse(null);

		// Master data for drop downs
		model.addAttribute("collections", collectionService.findAll());
		model.addAttribute("subseries", subserieService.findAll());
		model.addAttribute("audioEquipments", equipmentService.findAllByType(EquipmentType.AUDIO.getCode()));
		model.addAttribute("videoEquipments", equipmentService.findAllByType(EquipmentType.VIDEO.getCode()));

		model.addAttribute("transcribers",
				predefinedUserService.findAllByType(PredefinedUserType.TRANSCRIBER.getCode()));

		model.addAttribute("interviewdetails", transcription);

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

		return submitTranscription(newTranscription);
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

		return submitTranscription(newTranscription);
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

		System.out.println("status " + report.getStatus());

		System.out.println("borndigital " + report.getBornDigital());
		System.out.println("converted " + report.getConverted());

	
		List<Transcription> transcriptions = getTranscriptions(report);
////
//		if (report.getAction().equals("download")) {
//			downloadTranscriptions(transcriptions);
//		}
//
//		model.addAttribute("recordcount", transcriptions.size() + " records found.");
//
//		model.addAttribute("interviewers",
//				predefinedUserService.findAllByType(PredefinedUserType.INTERVIEWER.getCode()));

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

	@RequestMapping("export")
	public String export(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		return "export";
	}

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

			System.out.println("not Uploaded ");

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

	@PostMapping("/transactions/export")
	public ResponseEntity<Resource> export(Model model, HttpServletResponse response) throws IOException {

		return downloadTranscriptions(transcriptionService.findAll());

//		String filename = "transcriptions.xlsx";
//
//		InputStreamResource file = new InputStreamResource(
//				ExcelService.transcriptionToExcel());
//
//		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
//				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);

	}

	private ResponseEntity<Resource> downloadTranscriptions(List<Transcription> transcriptions) {
		String filename = "transcriptions.xlsx";

		InputStreamResource file = new InputStreamResource(ExcelService.transcriptionToExcel(transcriptions));

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}

}