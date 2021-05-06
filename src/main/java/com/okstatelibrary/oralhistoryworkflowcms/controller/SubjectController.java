package com.okstatelibrary.oralhistoryworkflowcms.controller;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.okstatelibrary.oralhistoryworkflowcms.entity.*;
import com.okstatelibrary.oralhistoryworkflowcms.enums.ErrorType;
import com.okstatelibrary.oralhistoryworkflowcms.service.*;
import com.okstatelibrary.oralhistoryworkflowcms.util.StringUtil;

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

		if (transcriptionService.isRecordsExistsBySuject(newSubject.getSubjectName())) {

			this.errorType = ErrorType.WARNING;
			this.message = "Cannot delete subject record, due to references!";
		} else {

			subjectService.deleteSubject(subject.getId());

			this.errorType = ErrorType.SUCESS;
			this.message = "Successfully deleted ssubject!";
		}

		return "redirect:/subjects";
	}

	@RequestMapping(value = "/validate", method = RequestMethod.POST)
	@ResponseBody
	public String validate(Model model, HttpServletRequest request) throws IOException {

		List<String> masterSubjects = subjectService.findAll().stream().map(Subject::getSubjectName)
				.collect(Collectors.toList());

		masterSubjects.replaceAll(String::toLowerCase);

		List<Transcription> transcriptions = transcriptionService.findAll();

		List<String> subjects = new ArrayList<String>();

		for (Transcription transcription : transcriptions) {

			if (StringUtil.isNotNullOrNotEmpty(transcription.getSubject())) {

				String[] transcriptionSubjects = transcription.getSubject().split(";");

				for (String transcriptionSubject : transcriptionSubjects) {

					String trimmedSubject = transcriptionSubject.trim().toLowerCase();

					if (!subjects.contains(trimmedSubject) && !masterSubjects.contains(trimmedSubject)
							&& StringUtil.isNotNullOrNotEmpty(trimmedSubject)) {
						subjects.add(trimmedSubject);
					}
				}
			}
		}

		HttpSession session = request.getSession();
		session.setAttribute("subjectsListObj", subjects);

		return subjects.size() + "";
	}

	@PostMapping("/submitall")
	public String submitall(HttpServletRequest request) throws IOException {

		HttpSession session = request.getSession();
		
		@SuppressWarnings("unchecked")
		List<String> subjects = (List<String>) session.getAttribute("subjectsListObj");

		for (String subjectName : subjects) {

			Subject subject = new Subject(
					subjectName.substring(0, 1).toUpperCase() + subjectName.substring(1).toLowerCase());

			subjectService.saveSubject(subject);
		}

		this.errorType = ErrorType.SUCESS;
		this.message = "Subject details added successfully!";

		return "redirect:/validate";
	}

}