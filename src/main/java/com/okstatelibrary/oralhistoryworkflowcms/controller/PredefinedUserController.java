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
import com.okstatelibrary.oralhistoryworkflowcms.enums.PredefinedUserType;
import com.okstatelibrary.oralhistoryworkflowcms.service.*;

@Controller
@RequestMapping("")
public class PredefinedUserController extends MainController {

	@Autowired
	private UserService userService;

	@Autowired
	private PredefinedUserService predefinedUserService;

	@Autowired
	private TranscriptionService transcriptionService;

	@RequestMapping("/interviewers")
	public String interviewers(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		List<PredefinedUser> predefineusers = predefinedUserService
				.findAllByType(PredefinedUserType.INTERVIEWER.getCode());

		model.addAttribute("usertype", "Interviewer");
		model.addAttribute("usertypeid", PredefinedUserType.INTERVIEWER.getCode());
		model.addAttribute("predefineusers", predefineusers);

		this.messageSetup(model);

		return "predefineusers";
	}

	@RequestMapping("/transcribers")
	public String transcribers(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		List<PredefinedUser> predefineusers = predefinedUserService
				.findAllByType(PredefinedUserType.TRANSCRIBER.getCode());

		model.addAttribute("usertype", "Transcriber");
		model.addAttribute("usertypeid", PredefinedUserType.TRANSCRIBER.getCode());
		model.addAttribute("predefineusers", predefineusers);

		this.messageSetup(model);

		return "predefineusers";
	}

	@RequestMapping(value = { "predefineuser/add/{type}" })
	public String add(@PathVariable(required = false) String type, Principal principal, Model model) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		PredefinedUser predefineUser = new PredefinedUser(Short.parseShort("0"), Byte.parseByte(type));

		model.addAttribute("usertype", "Transcriber ");

		if (Byte.parseByte(type) == PredefinedUserType.INTERVIEWER.getCode()) {

			model.addAttribute("usertype", "Interviewer ");
		}

		model.addAttribute("predefineuserdetails", predefineUser);

		return "predefineuserdetails";
	}

	@RequestMapping(value = { "predefineuser/{id}" })
	public String details(@PathVariable(required = false) String id, Principal principal, Model model) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		PredefinedUser predefineUser = predefinedUserService.findById(Short.parseShort(id)).orElse(null);

		model.addAttribute("usertype", "Transcriber ");

		if (predefineUser.getUserType() == PredefinedUserType.INTERVIEWER.getCode()) {

			model.addAttribute("usertype", "Interviewer ");
		}
		
		model.addAttribute("predefineuserdetails", predefineUser);

		return "predefineuserdetails";
	}

	@PostMapping("/predefineuser/update")
	public String update(@ModelAttribute("predefinedUser") PredefinedUser predefinedUser, Model model,
			Principal principal) {

		PredefinedUser newPredefinedUser = null;

		if (predefinedUser.getId() > 0) {

			newPredefinedUser = predefinedUserService.findById(predefinedUser.getId()).orElse(null);

			newPredefinedUser.setName(predefinedUser.getName());

		} else {
			newPredefinedUser = predefinedUser;
		}

		PredefinedUser predefineUser = predefinedUserService.savePredefinedUser(newPredefinedUser);

		String returnSection = "redirect:/transcribers";
		String selectedUserType = "Transcriber";

		if (predefineUser.getUserType() == PredefinedUserType.INTERVIEWER.getCode()) {
			returnSection = "redirect:/interviewers";
			selectedUserType = "Interviewers";
		}

		if (predefineUser != null && predefineUser.getId() > 0) {

			this.errorType = ErrorType.SUCESS;
			this.message = selectedUserType + " details changed successfully!";

		} else {
			this.errorType = ErrorType.ERROR;
			this.message = "Failed to add " + selectedUserType + "!";
		}

		return returnSection;
	}

	@PostMapping("/predefineuser/delete")
	public String delete(@ModelAttribute("predefinedUser") PredefinedUser predefinedUser, Model model) {

		predefinedUser = predefinedUserService.findById(predefinedUser.getId()).orElse(null);

		List<Transcription> transcriptions = null;
		String type = "";
		String returnSection = "";

		if (predefinedUser.getUserType() == PredefinedUserType.INTERVIEWER.getCode()) {
			transcriptions = transcriptionService.findByInterviewerName(predefinedUser.getName());
			type = "interviewer";
			returnSection = "redirect:/interviewers";
		} else {
			transcriptions = transcriptionService.findByTranscriberName(predefinedUser.getName());
			type = "transcriber";
			returnSection = "redirect:/transcribers";
		}

		if (transcriptions != null && transcriptions.size() > 0) {

			this.errorType = ErrorType.WARNING;
			this.message = "Cannot delete " + type + " record, due to references!";
		} else {

			predefinedUserService.deletePredefinedUser(predefinedUser.getId());

			this.errorType = ErrorType.SUCESS;
			this.message = "Successfully deleted " + type + "";
		}

		return returnSection;
	}

}