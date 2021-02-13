package com.okstatelibrary.oralhistoryworkflowcms.controller;

import java.security.Principal;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.okstatelibrary.oralhistoryworkflowcms.entity.*;
import com.okstatelibrary.oralhistoryworkflowcms.enums.ErrorType;
import com.okstatelibrary.oralhistoryworkflowcms.service.*;

@Controller
@RequestMapping("subjects")
public class SubjectController extends MainController {

	@Autowired
	private UserService userService;

	@Autowired
	private SubjectService subjectService;

	@Autowired
	private TranscriptionService transcriptionService;

	@RequestMapping("")
	public String repositories(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		model.addAttribute("subjects", subjectService.findAll());

		this.messageSetup(model);

		return "subjects";
	}

	@RequestMapping(value = { "/add" })
	public String add(@PathVariable(required = false) String id, Principal principal, Model model) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		Subject subject = new Subject(Short.parseShort("0"));

		model.addAttribute("subjectdetails", subject);

		return "subjectdetails";
	}

	@RequestMapping(value = { "/{id}" })
	public String details(@PathVariable(required = false) String id, Principal principal, Model model) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		Subject subject = subjectService.findById(Short.parseShort(id)).orElse(null);

		model.addAttribute("subjectdetails", subject);

		return "subjectdetails";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute("subject") Subject subject, Model model, Principal principal) {

		User user = userService.findByUsername(principal.getName());

		Subject newSuject = null;

		if (subject.getId() > 0) {

			newSuject = subjectService.findById(subject.getId()).orElse(null);

			newSuject.setSubjectName(subject.getSubjectName());

		} else {
			newSuject = subject;
		}

		Subject sub = subjectService.saveSubject(newSuject);

		if (sub != null && sub.getId() > 0) {

			this.errorType = ErrorType.SUCESS;
			this.message = "Subject details changed successfully!";

		} else {
			this.errorType = ErrorType.ERROR;
			this.message = "Failed to add subject!";
		}

		return "redirect:/subjects";
	}

	@PostMapping("/delete")
	public String delete(@ModelAttribute("subject") Subject subject, Model model) {

		Subject newSubject = subjectService.findById(subject.getId()).orElse(null);

		List<Transcription> transcriptions = transcriptionService.findBySuject(newSubject.getSubjectName());

		System.out.print(transcriptions.size());
		
		if (transcriptions != null && transcriptions.size() > 0) {

			this.errorType = ErrorType.WARNING;
			this.message = "Cannot delete subject record, due to references!";
		} else {

			subjectService.deleteSubject(subject.getId());

			this.errorType = ErrorType.SUCESS;
			this.message = "Successfully deleted ssubject!";
		}

		return "redirect:/subjects";
	}

}