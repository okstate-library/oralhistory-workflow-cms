package com.okstatelibrary.oralhistoryworkflowcms.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import com.okstatelibrary.oralhistoryworkflowcms.service.Impl.ElasticsearchSyncService;
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
import org.springframework.util.ResourceUtils;
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
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
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
	private ElasticsearchSyncService elasticsearchSyncService;

	@Autowired
	private CollectionService collectionService;

	@Autowired
	private MediumService mediumService;

	@Autowired
	private PredefinedUserService predefinedUserService;

	@Autowired
	private EquipmentService equipmentService;

	@Autowired
	private SeriesService seriesService;

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

	@RequestMapping("/browse")
	public String browse(Model model, Principal principal, HttpServletRequest request,
			@RequestParam(name = "search", required = false) String search) throws ParseException {

		HttpSession session = request.getSession();
		session.setAttribute("searchword", search);

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		this.messageSetup(model);

		return "browse";
	}

	@Lazy
	@RequestMapping(value = "/browse/data", method = RequestMethod.GET)
	public @ResponseBody BrowsePageObject browseData(@RequestParam(required = false) String collections,
			@RequestParam(required = false) String series, @RequestParam(required = false) String interviewers,
			@RequestParam(required = false) String subjects, @RequestParam(required = false) String onlineoffline,
			@RequestParam(required = false) String darkarchive, Model model, HttpServletRequest request)
			throws JsonProcessingException {

		BrowsePageObject browsePageObject = new BrowsePageObject();

		HttpSession session = request.getSession();

		browsePageObject.setSearchWord((String) session.getAttribute("searchword"));

		if (StringUtil.isNotNullOrNotEmpty(collections) || StringUtil.isNotNullOrNotEmpty(series)
				|| StringUtil.isNotNullOrNotEmpty(interviewers) || StringUtil.isNotNullOrNotEmpty(subjects)
				|| StringUtil.isNotNullOrNotEmpty(darkarchive) || StringUtil.isNotNullOrNotEmpty(onlineoffline)) {

			List<Transcription> transcripts = transcriptionService.findAll(collections, series, interviewers, subjects,
					darkarchive, onlineoffline);

			browsePageObject.setTranscriptions(transcripts);

			List<CheckBoxObject> selectedCollections = getDistinctCollections(transcripts);

			browsePageObject.setCollections(selectedCollections);

			browsePageObject.setSeries(getDistinctSeries(transcripts, selectedCollections.size()));

			browsePageObject.setInterviewers(getDistinctInterviewers(transcripts));

			browsePageObject.setSubjects(getDistinctSubjects(transcripts));

			browsePageObject.setStatusOnline(getStatusOnline(transcripts));

			browsePageObject.setDarkArchieve(getDarkArchieve(transcripts));

		} else {

			browsePageObject.setTranscriptions(transcriptionService.findAll());

			browsePageObject.setCollections(ConvertCollectionToCheckBoxObject(collectionService.findAll()));

			browsePageObject.setSeries(ConvertSeriesToCheckBoxObject(seriesService.findAll()));

			browsePageObject.setInterviewers(ConvertInterviewersToCheckBoxObject(
					predefinedUserService.findAllByType(PredefinedUserType.INTERVIEWER.getCode())));

			browsePageObject.setSubjects(getDistinctSubjects(transcriptionService.findAll()));

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

	private List<CheckBoxObject> getDistinctSeries(List<Transcription> transcripts, int collectionSize) {

		Map<Series, Long> newTranscripts = transcripts.stream()
				.collect(Collectors.groupingBy(Transcription::getseries, Collectors.counting()));

		List<CheckBoxObject> checkBoxObjectList = new ArrayList<CheckBoxObject>();

		int numberOfSeries = newTranscripts.size();

		for (Map.Entry<Series, Long> entry : newTranscripts.entrySet()) {

			Series series = entry.getKey();

			if (collectionSize == 1 && numberOfSeries > 1) {
				checkBoxObjectList.add(new CheckBoxObject(series.getId(), series.getSeriesName(),
						series.getSeriesName(), entry.getValue()));
			} else {
				if (!series.getSeriesName().toLowerCase().contentEquals(StringUtil.NotAvaliable)) {

					checkBoxObjectList.add(new CheckBoxObject(series.getId(), series.getSeriesName(),
							series.getSeriesName(), entry.getValue()));
				}
			}

		}

		return checkBoxObjectList;
	}

	private List<CheckBoxObject> ConvertSeriesToCheckBoxObject(List<Series> series) {

		List<CheckBoxObject> checkBoxObjectList = new ArrayList<CheckBoxObject>();

		for (Series aseries : series) {

			if (!aseries.getSeriesName().toLowerCase().contentEquals(StringUtil.NotAvaliable)) {

				checkBoxObjectList
						.add(new CheckBoxObject(aseries.getId(), aseries.getSeriesName(), aseries.getSeriesName()));
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

		LinkedHashMap<String, Long> sortedTranscripts = new LinkedHashMap<>();

		newTranscripts.entrySet().stream().sorted(Map.Entry.comparingByValue((Comparator.reverseOrder())))
				.forEachOrdered(x -> sortedTranscripts.put(x.getKey(), x.getValue()));

		int i = 0;

		for (Map.Entry<String, Long> entry : sortedTranscripts.entrySet()) {

			checkBoxObjectList.add(new CheckBoxObject(++i, entry.getKey(), entry.getKey(), entry.getValue()));
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
		model.addAttribute("series", seriesService.findAll());

		model.addAttribute("audioEquipments", equipmentService.findAllByType(EquipmentType.AUDIO.getCode()));
		model.addAttribute("videoEquipments", equipmentService.findAllByType(EquipmentType.VIDEO.getCode()));
		model.addAttribute("digitalCameraEquipments",
				equipmentService.findAllByType(EquipmentType.DIGITALCAMERA.getCode()));

		model.addAttribute("transcribers",
				predefinedUserService.findAllByType(PredefinedUserType.TRANSCRIBER.getCode()));

		List<PredefinedUser> interviewers = predefinedUserService
				.findAllByType(PredefinedUserType.INTERVIEWER.getCode());

		model.addAttribute("interviewers", sortByPreference(interviewers));

		model.addAttribute("transcription", new Transcription());

		this.messageSetup(model);

		return "addinterview";
	}

	private List<PredefinedUser> sortByPreference(List<PredefinedUser> interviewers) {

		List<PredefinedUser> newInterviewers = new ArrayList<PredefinedUser>(interviewers);

		List<PredefinedUser> selectedList = new ArrayList<PredefinedUser>();

		String[] selectedNames = new String[] { "Brown, Autumn", "Craine, Tracy", "Daglaris, Patrick", "Finchum, Tanya",
				"Little Thunder, Julie Pearson", "Milligan, Sarah", "Neurohr, Karen" };

		List<String> namesList = Arrays.asList(selectedNames);

		for (PredefinedUser interviewer : interviewers) {

			if (namesList.contains(interviewer.getName())) {
				selectedList.add(interviewer);

				newInterviewers.remove(interviewer);
			}
		}

		Collections.sort(selectedList, Comparator.comparing(PredefinedUser::getName));

		Collections.sort(newInterviewers, Comparator.comparing(PredefinedUser::getName));

		return Stream.concat(selectedList.stream(), newInterviewers.stream()).collect(Collectors.toList());
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

			transcription.setInterviewer(setDelimeterdInterviewers(transcription.getInterviewer()));

			transcription.setInterviewDates(transcription.getInterviewDates());
			transcription.setAccessionDate(transcription.getAccessionDate());
			transcription.setAccessionNote(transcription.getAccessionNote());

			transcription.setCreatedDate(DateUtil.getTodaySqlDate());
			transcription.setUpdatedDate(DateUtil.getTodaySqlDate());

			transcription.setCreatedBy(Integer.parseInt(user.getUserId().toString()));
			transcription.setUpdatedBy(Integer.parseInt(user.getUserId().toString()));

			Series series = seriesService.findById(transcription.getSeriesId()).orElse(null);

			if (!series.getSeriesName().toLowerCase().equals(StringUtil.NotAvaliable)) {

				transcription.setLanguage(series.getLanguage());
				transcription.setRights(series.getRights());
				transcription.setScopeAndContents(series.getScopeAndContent());
				transcription.setPublisher(series.getPublisher());
				transcription.setFormat(series.getFormat());
				transcription.setType(series.getType());

			} else {

				Collection collection = collectionService.findById(transcription.getCollectionId()).orElse(null);

				transcription.setLanguage(collection.getLanguage());
				transcription.setRights(collection.getRights());
				transcription.setScopeAndContents(collection.getScopeAndContent());
				transcription.setPublisher(collection.getPublisher());
				transcription.setFormat(collection.getFormat());
				transcription.setType(collection.getType());
			}

			// Read the JSON file that contains default values and fill the
			// new interview record.
			Gson gson = new Gson();

			JsonReader reader = null;

			File file = null;

			try {

				file = ResourceUtils.getFile("classpath:static/assets/defaultvalues.json");

				reader = new JsonReader(new FileReader(file));

				DefaultValues data = gson.fromJson(reader, DefaultValues.class);

				transcription.setRights(data.getRights());
				transcription.setPublisher(data.getPublisher());
				transcription.setFormat(data.getFormat());
				transcription.setType(data.getType());
				transcription.setLanguage(data.getLanguage());

			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}

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

	@RequestMapping(value = "/bulkmodify", method = RequestMethod.GET)
	public String bulkmodify(Model model, Principal principal, @Valid String ids, HttpServletRequest request)
			throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		model.addAttribute("collections", collectionService.findAll());
		model.addAttribute("series", seriesService.findAll());

		List<Transcription> transcriptionList = new ArrayList<Transcription>();

		HttpSession session = request.getSession();
		session.setAttribute("bulkIds", ids);

		for (String id : ids.split(",")) {
			transcriptionList.add(transcriptionService.findById(Integer.parseInt(id)).orElse(null));
		}

		model.addAttribute("transcriptions", transcriptionList);

		this.messageSetup(model);

		return "bulkmodify";
	}

	@RequestMapping(value = "/bulkmodify", method = RequestMethod.POST)
	public String bulkmodifyPost(@Valid Transcription transcription, BindingResult bindingResult, Model model,
			Principal principal, HttpServletRequest request) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		HttpSession session = request.getSession();
		String ids = (String) session.getAttribute("bulkIds");

		for (String id : ids.split(",")) {

			Transcription newTranscription = transcriptionService.findById(Integer.parseInt(id)).orElse(null);

			if (transcription.getCollectionId() > 0 && transcription.getSeriesId() > 0) {
				newTranscription.setCollectionId(transcription.getCollectionId());
				newTranscription.setSeriesId(transcription.getSeriesId());
			}

			newTranscription.setIsPublic(transcription.getIsPublic());
			newTranscription.setSentOut(transcription.getSentOut());
			newTranscription.setIsProductionMasterFileStatus(transcription.getIsProductionMasterFileStatus());
			newTranscription.setIsOnline(transcription.getIsOnline());
			newTranscription.setIsRosetta(transcription.getIsRosetta());

			newTranscription.setUpdatedDate(DateUtil.getTodaySqlDate());
			newTranscription.setUpdatedBy(Integer.parseInt(user.getUserId().toString()));

			transcriptionService.save(newTranscription);
		}

		this.errorType = ErrorType.SUCESS;
		this.message = "Bulk records successfully modified!";

		return "redirect:/browse";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String deleteinterview(@Valid Transcription transcription, BindingResult bindingResult, Model model,
			Principal principal) throws ParseException {

		transcriptionService.delete(transcription.getId());

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
			model.addAttribute("series", seriesService.findAll());
			model.addAttribute("audioEquipments", equipmentService.findAllByType(EquipmentType.AUDIO.getCode()));
			model.addAttribute("videoEquipments", equipmentService.findAllByType(EquipmentType.VIDEO.getCode()));
			model.addAttribute("digitalCameraEquipments",
					equipmentService.findAllByType(EquipmentType.DIGITALCAMERA.getCode()));

			model.addAttribute("interviews", transcriptionService.findAll(true));

			model.addAttribute("transcribers",
					predefinedUserService.findAllByType(PredefinedUserType.TRANSCRIBER.getCode()));

			model.addAttribute("interviewdetails", transcription);

			model.addAttribute("mediums", mediumService.findAll());

			model.addAttribute("interviewers",
					sortByPreference(predefinedUserService.findAllByType(PredefinedUserType.INTERVIEWER.getCode())));

			model.addAttribute("subjects", subjectService.findAll());

		} else {
			this.errorType = ErrorType.ERROR;
			this.message = "Relevent interview record cannot find in the system. Please refresh the 'Browse' or"
					+ " 'Transcription queue' page and look for the record.";
		}

		this.messageSetup(model);

		return "modifyinterview";
	}

	@RequestMapping(value = "/modifytranscript", method = RequestMethod.POST)
	public String modifytranscript(@Valid Transcription transcription, BindingResult bindingResult, Model model,
			Principal principal) throws ParseException {

		Transcription newTranscription = transcriptionService.findById(transcription.getId()).orElse(null);

//		// Title
//		newTranscription.setTranscriptStatus(transcription.getTranscriptStatus());

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

		// Is Narrator Received
		newTranscription.setIsNarratorRevisionReceived(transcription.getIsNarratorRevisionReceived());

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

		// Recording format
		newTranscription.setIsAudioFormat(transcription.getIsAudioFormat());
		newTranscription.setIsVideoFormat(transcription.getIsVideoFormat());

		// Equipment Number
		newTranscription.setEquipmentNumber(transcription.getEquipmentNumber());

		// Audio and video equipment used
		newTranscription.setAudioEquipmentUsed(transcription.getAudioEquipmentUsed());
		newTranscription.setVideoEquipmentUsed(transcription.getVideoEquipmentUsed());

		// Audio and video original duration
		newTranscription.setOriginalAudioDuration(transcription.getOriginalAudioDuration());
		newTranscription.setOriginalVideoDuration(transcription.getOriginalVideoDuration());

		// Audio and video edited duration
		newTranscription.setEditedAudioDuration(transcription.getEditedAudioDuration());
		newTranscription.setEditedVideoDuration(transcription.getEditedVideoDuration());

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

		newTranscription.setMediaNote(transcription.getMediaNote());

		// Digitization Note
		newTranscription.setDigitizationNote(transcription.getDigitizationNote());

		// Transcript Size
		newTranscription.setTranscriptSize(transcription.getTranscriptSize());

		// Images details
		newTranscription.setDigitalCameraUsed(transcription.getDigitalCameraUsed());
		newTranscription.setNumberOfImages(transcription.getNumberOfImages());
		newTranscription.setImagesNote(transcription.getImagesNote());

		// Documents details
		newTranscription.setNumberOfDocuments(transcription.getNumberOfDocuments());
		newTranscription.setDocumentsNote(transcription.getDocumentsNote());

		// Access media status
		newTranscription.setIsProductionMasterFileStatus(transcription.getIsProductionMasterFileStatus());

		// Specification and file locations
		newTranscription.setTechnicalSpecification(transcription.getTechnicalSpecification());

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

		// RelatedInterviews
		newTranscription.setRelatedInterviews(StringUtil.isNotNullOrNotEmpty(transcription.getRelatedInterviews())
				? transcription.getRelatedInterviews().replaceAll(",", "; ")
				: "");

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

		// OnlineDate
		newTranscription.setOnlineDate(transcription.getOnlineDate());

		// IsRosetta
		newTranscription.setIsRosetta(transcription.getIsRosetta());

		// Release Form
		newTranscription.setReleaseForm(transcription.getReleaseForm());

		// IsPublic
		newTranscription.setIsPublic(transcription.getIsPublic());

		// Is Restriction
		newTranscription.setIsRestriction(transcription.getIsRestriction());

		// Restriction Note
		newTranscription.setRestrictionNote(transcription.getRestrictionNote());

		// Is Dark Archive
		newTranscription.setIsDarkArchive(transcription.getIsDarkArchive());

		// Dark Archive Note
		newTranscription.setDarkArchiveNote(transcription.getDarkArchiveNote());

		// CONTENTdmURL
		newTranscription.setCONTENTdmURL(transcription.getCONTENTdmURL());

		// YouTube URL
		newTranscription.setYouTubeURL(transcription.getYouTubeURL());

		// OHMS URL
		newTranscription.setOHMSURL(transcription.getOHMSURL());

		// TranscriptLocation
		newTranscription.setTranscriptLocation(transcription.getTranscriptLocation());

		// Accession Date
		newTranscription.setAccessionDate(transcription.getAccessionDate());

		// Accession Note
		newTranscription.setAccessionNote(transcription.getAccessionNote());

		// General Note
		newTranscription.setGeneralNote(transcription.getGeneralNote());

		// Narrator Bio
		newTranscription.setNarratorBio(transcription.getNarratorBio());

		// Usage Information
		newTranscription.setUsageInformation(transcription.getUsageInformation());

		return submitTranscription(newTranscription);
	}

	@RequestMapping(value = "/modifyfieldnotes", method = RequestMethod.POST)
	public String modifyfieldnotes(@Valid Transcription transcription, BindingResult bindingResult, Model model,
			Principal principal) throws ParseException {

		Transcription newTranscription = transcriptionService.findById(transcription.getId()).orElse(null);

		// Interviewer Description
		newTranscription.setInterviewerDescription(transcription.getInterviewerDescription());

		// Interviewer Keywords
		newTranscription.setInterviewerKeywords(transcription.getInterviewerKeywords());

		// Interviewer Subjects
		newTranscription.setInterviewerSubjects(transcription.getInterviewerSubjects());

		// IsPotentiallySensitiveContent
		newTranscription.setIsPotentiallySensitiveContent(transcription.getIsPotentiallySensitiveContent());

		// SensitiveContentDescription
		newTranscription.setSensitiveContentDescription(transcription.getSensitiveContentDescription());

		// Interviewer Note
		newTranscription.setInterviewerNote(transcription.getInterviewerNote());

		// General Note
		newTranscription.setInterviewContext(transcription.getInterviewContext());

		return submitTranscription(newTranscription);
	}

	private String submitTranscription(Transcription newTranscription) {

		this.errorType = ErrorType.SUCESS;
		this.message = "Interview details updated successfully!";

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

	@PostMapping("/downloadReportsForImport")
	public ResponseEntity<Resource> downloadReportsForImport(HttpServletRequest request) throws IOException {

		HttpSession session = request.getSession();

		Report report = (Report) session.getAttribute("reportobj");

		return downloadTranscriptionsForImport(getTranscriptions(report));
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

		transcriptions = transcriptionService.findAll();

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

		List<String> requiredFeildsNullRecords = new ArrayList<String>();
		List<String> projectCodeExists = new ArrayList<String>();
		List<String> collectionNames = new ArrayList<String>();
		List<String> seriesNames = new ArrayList<String>();

		int insertRecordCount = 0;

		// save the file on the local file system
		try {

			Path path = Paths.get(UPLOAD_DIR + fileName);

			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			List<Transcription> transcriptions = ExcelService.getTranscriptionFromExcel(path.toString());

			User user = userService.findByUsername(principal.getName());
			model.addAttribute("user", user);

			if (transcriptions != null && transcriptions.size() > 1) {

				int index = 0;

				for (Transcription transcription : transcriptions) {

					try {

						index++;

						if (!StringUtil.isNotNullOrNotEmpty(transcription.getProjectCode())
								|| !StringUtil.isNotNullOrNotEmpty(transcription.getInterviewer())
								|| !StringUtil.isNotNullOrNotEmpty(transcription.getInterviewee())
								|| !StringUtil.isNotNullOrNotEmpty(transcription.getInterviewDates())
								|| !StringUtil.isNotNullOrNotEmpty(transcription.getCollectionName())
								|| transcription.getReleaseForm() == (byte) -1) {

							requiredFeildsNullRecords.add(index + "");

						} else {
							if (transcriptionService.isProjectCodeExists(transcription.getProjectCode())) {

								projectCodeExists.add(transcription.getProjectCode());

								// errorMessage += "Row id " + index + " project code exists.";

							} else {
								// Setup collection using collection name
								Collection collection = collectionService.findByName(transcription.getCollectionName());

								if (collection == null) {

									collectionNames.add(transcription.getCollectionName());

								} else {

									transcription.setCollectionId(collection.getId());

									Series aseries = seriesService.findByName(collection.getId(),
											transcription.getSeriesName());

									if (aseries != null) {

										transcription.setSeriesId(aseries.getId());

										transcription.setCreatedDate(DateUtil.getTodaySqlDate());
										transcription.setUpdatedDate(DateUtil.getTodaySqlDate());

										transcription.setCreatedBy(Integer.parseInt(user.getUserId().toString()));
										transcription.setUpdatedBy(Integer.parseInt(user.getUserId().toString()));

										transcriptionService.save(transcription);

										insertRecordCount++;

									} else {

										seriesNames.add(transcription.getSeriesName());

									}
								}

							}

						}

					} catch (Exception e) {

						System.out.println(index + "Error found " + e.getMessage() + " " + e.toString());

					}
				}
			}

		} catch (

		IOException e) {
			e.printStackTrace();
		}

		this.errorType = ErrorType.SUCESS;
		this.message = insertRecordCount + " record(s) successfully import";

		String msg = (requiredFeildsNullRecords.size() > 0 ? "Excel index(es) " : "")
				+ String.join(",", requiredFeildsNullRecords);

		model.addAttribute("requiredFeildsNullRecords", msg);
		model.addAttribute("projectCodeExists", String.join(",", (new HashSet<String>(projectCodeExists)).toString()));
		model.addAttribute("collectionNames", String.join(",", (new HashSet<String>(collectionNames)).toString()));
		// model.addAttribute("seriesNames", String.join(",", (new
		// HashSet<String>(seriesNames)).toString()));

		this.messageSetup(model);
		return "import";
	}

	@PostMapping("/uploadExisting")
	public String uploadExisting(@RequestParam("file") MultipartFile file, RedirectAttributes attributes, Model model,
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

		List<String> requiredFeildsNullRecords = new ArrayList<String>();
		List<String> projectCodeExists = new ArrayList<String>();
		List<String> collectionNames = new ArrayList<String>();
		List<String> seriesNames = new ArrayList<String>();

		int insertRecordCount = 0;

		// save the file on the local file system
		try {

			Path path = Paths.get(UPLOAD_DIR + fileName);

			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			List<Transcription> transcriptions = ExcelService.getExistingTranscriptionFromExcel(path.toString());

			User user = userService.findByUsername(principal.getName());
			model.addAttribute("user", user);

			if (transcriptions != null && transcriptions.size() >= 1) {

				int index = 0;

				for (Transcription transcription : transcriptions) {

					try {

						System.out.println("Interviewer" + transcription.getInterviewer());

						index++;

						if (!StringUtil.isNotNullOrNotEmpty(transcription.getProjectCode())
								|| !StringUtil.isNotNullOrNotEmpty(transcription.getInterviewer())
								|| !StringUtil.isNotNullOrNotEmpty(transcription.getInterviewee())
								|| !StringUtil.isNotNullOrNotEmpty(transcription.getInterviewDates())
								|| !StringUtil.isNotNullOrNotEmpty(transcription.getCollectionName())
								|| transcription.getReleaseForm() == (byte) -1) {

							requiredFeildsNullRecords.add(index + "");

						} else {

							// Setup collection using collection name
							Collection collection = collectionService.findByName(transcription.getCollectionName());

							if (collection == null) {

								collectionNames.add(transcription.getCollectionName());

							} else {

								// transcription.setCollectionId(collection.getId());

								Series aseries = seriesService.findByName(collection.getId(),
										transcription.getSeriesName());

								if (aseries != null) {

									Transcription existingTranscription = transcriptionService
											.findById(transcription.getId()).orElse(null);

									if (existingTranscription != null) {

										existingTranscription.setCollectionId(collection.getId());
										existingTranscription.setSeriesId(aseries.getId());
										existingTranscription.setProjectCode(transcription.getProjectCode());
										existingTranscription.setInterviewDates(transcription.getInterviewDates());
										existingTranscription.setInterviewee(transcription.getInterviewee());

										existingTranscription.setInterviewer(transcription.getInterviewer());
										existingTranscription.setReleaseForm(transcription.getReleaseForm());
										existingTranscription.setFormat(transcription.getFormat());
										existingTranscription.setDescription(transcription.getDescription());
										existingTranscription.setSubject(transcription.getSubject());

										existingTranscription.setKeywords(transcription.getKeywords());
										existingTranscription
												.setTranscriptLocation(transcription.getTranscriptLocation());
										existingTranscription.setType(transcription.getType());

										existingTranscription.setUpdatedDate(DateUtil.getTodaySqlDate());
										existingTranscription
												.setUpdatedBy(Integer.parseInt(user.getUserId().toString()));

										transcriptionService.save(existingTranscription);

										insertRecordCount++;

									}

								} else {

									seriesNames.add(transcription.getSeriesName());

								}
							}

						}

					} catch (Exception e) {

						System.out.println(index + "Error found " + e.getMessage() + " " + e.toString());

					}
				}
			}

		} catch (

		IOException e) {
			e.printStackTrace();
		}

		this.errorType = ErrorType.SUCESS;
		this.message = insertRecordCount + " record(s) successfully import";

		String msg = (requiredFeildsNullRecords.size() > 0 ? "Excel index(es) " : "")
				+ String.join(",", requiredFeildsNullRecords);

		model.addAttribute("requiredFeildsNullRecords", msg);
		model.addAttribute("projectCodeExists", String.join(",", (new HashSet<String>(projectCodeExists)).toString()));
		model.addAttribute("collectionNames", String.join(",", (new HashSet<String>(collectionNames)).toString()));
		// model.addAttribute("seriesNames", String.join(",", (new
		// HashSet<String>(seriesNames)).toString()));

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

	private ResponseEntity<Resource> downloadTranscriptionsForImport(List<Transcription> transcriptions) {
		String filename = "transcriptions.xlsx";

		InputStreamResource file = new InputStreamResource(ExcelService.transcriptionToExcelForImport(transcriptions));

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}


	@RequestMapping(value = "/browse/sync", method = RequestMethod.POST)
	public String sync(Model model, HttpServletRequest request, Principal principal) throws IOException {

		System.out.println("browse/sync");

		User user = userService.findByUsername(principal.getName());

		//elasticsearchSyncService = new ElasticsearchSyncService();

		elasticsearchSyncService.synchronizeData();


		this.errorType = ErrorType.SUCESS;
		this.message = "Series records validated!";

		return "redirect:/validate";
	}

}