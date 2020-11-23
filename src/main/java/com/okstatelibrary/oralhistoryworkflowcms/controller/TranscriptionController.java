package com.okstatelibrary.oralhistoryworkflowcms.controller;

import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okstatelibrary.oralhistoryworkflowcms.entity.*;
import com.okstatelibrary.oralhistoryworkflowcms.service.*;
import com.okstatelibrary.oralhistoryworkflowcms.util.StringUtil;

@Controller
@RequestMapping("")
public class TranscriptionController {

	@Autowired
	private UserService userService;

	@Autowired
	private TranscriptionService transcriptionService;

	@Autowired
	private CollectionService collectionService;

	@Autowired
	private SubserieService subserieService;

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

	@GetMapping("/index")
	public String index(Principal principal, Model model) throws ParseException {

		User user = null;

		if (principal != null) {
			user = userService.findByUsername(principal.getName());
			model.addAttribute("transcriptions", transcriptionService.findRecentlyAdded(user.getUserId()));
		}

		model.addAttribute("user", user);

		return "index";
	}

	@RequestMapping("/browse")
	public String browse(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		model.addAttribute("collections", collectionService.findAll());
		model.addAttribute("subseries", subserieService.findAll());

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
		
		//return null;
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
	public @ResponseBody List<Transcription> transcriptionQueueData(@RequestParam(required = false) String options)
			throws JsonProcessingException {

		System.out.println("options" + options);

		boolean isPriority = false, isTranscribed = false, isAuditCheck = false, isFirstEdit = false,
				isSecondEdit = false, isDraftSent = false, isCorrections = false, isFinalEdit = false,
				isSsentOut = false;

		if (options != null && !options.isBlank() && !options.isEmpty()) {

			isPriority = options.contains("2");
			isTranscribed = options.contains("3");
			isAuditCheck = options.contains("4");
			isFirstEdit = options.contains("5");
			isSecondEdit = options.contains("6");
			isDraftSent = options.contains("7");
			isCorrections = options.contains("8");
			isFinalEdit = options.contains("9");
			isSsentOut = options.contains("10");
		}

		List<Transcription> list = transcriptionService.findAll(false, isPriority, isTranscribed, isAuditCheck,
				isFirstEdit, isSecondEdit, isDraftSent, isCorrections, isFinalEdit, isSsentOut);

		return list;
	}

	@RequestMapping("/addinterview")
	public String addinterview(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		// model.addAttribute("transcriptions", transcriptionService.findAll(false));

		model.addAttribute("collections", collectionService.findAll());

		return "addinterview";
	}

	@RequestMapping(value = "/getSubseries", method = RequestMethod.GET)
	public @ResponseBody List<Subserie> getSubseries(@RequestParam(value = "collectionId") short collectionId) {

		List<Subserie> list = subserieService.findAllbyCollectionId(collectionId);
//		
//		System.out.println("list size---" + list.size());

		// Collection collection =
		// collectionService.findById(collectionId).orElse(null);

		for (Subserie sub : list) {
			System.out.println(sub.getId() + sub.getSubseriesName());
		}

		return list;
	}

	@RequestMapping(value = "/addinterview", method = RequestMethod.POST)
	public String addinterviewPost(@ModelAttribute Transcription transcription, Model model, Principal principal)
			throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		System.out.println("transcription - Colleciotn" + transcription.getCollectionId());

		return "addinterview";
	}

	@RequestMapping("/reports")
	public String reports(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		// model.addAttribute("transcriptions", transcriptionService.findAll(false));

		return "reports";
	}

}