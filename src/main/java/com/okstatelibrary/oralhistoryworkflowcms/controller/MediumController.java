package com.okstatelibrary.oralhistoryworkflowcms.controller;

import java.security.Principal;
import java.text.ParseException;

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
@RequestMapping("mediums")
public class MediumController extends MainController {

	@Autowired
	private UserService userService;

	@Autowired
	private MediumService mediumService;

	@Autowired
	private TranscriptionService transcriptionService;

	@RequestMapping("")
	public String mediums(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		model.addAttribute("mediums", mediumService.findAll());

		this.messageSetup(model);

		return "mediums";
	}

	@RequestMapping(value = { "/add" })
	public String add(@PathVariable(required = false) String id, Principal principal, Model model) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		Medium medium = new Medium(Short.parseShort("0"));

		model.addAttribute("mediumdetails", medium);

		return "mediumdetails";
	}

	@RequestMapping(value = { "/{id}" })
	public String details(@PathVariable(required = false) String id, Principal principal, Model model) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		Medium medium = mediumService.findById(Short.parseShort(id)).orElse(null);

		model.addAttribute("mediumdetails", medium);

		return "mediumdetails";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute("medium") Medium medium, Model model, Principal principal) {

		Medium nMedium = mediumService.saveEquipment(medium);

		if (nMedium != null && nMedium.getId() > 0) {

			this.errorType = ErrorType.SUCESS;
			this.message = "Medium details changed successfully!";

		} else {
			this.errorType = ErrorType.ERROR;
			this.message = "Failed to add medium!";
		}

		return "redirect:/mediums";
	}

	@PostMapping("/delete")
	public String delete(@ModelAttribute("mediums") Medium medium, Model model) {

		if (transcriptionService.isRecordsExistsByOriginalMedium(medium.getMediumName())) {

			this.errorType = ErrorType.WARNING;
			this.message = "Cannot delete medium record, due to references!";
		} else {

			mediumService.deleteMedium(medium.getId());

			this.errorType = ErrorType.SUCESS;
			this.message = "Successfully deleted medium record!";
		}

		return "redirect:/mediums";
	}

}