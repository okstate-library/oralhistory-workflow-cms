package com.okstatelibrary.oralhistoryworkflowcms.controller;

import java.security.Principal;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.okstatelibrary.oralhistoryworkflowcms.entity.*;
import com.okstatelibrary.oralhistoryworkflowcms.enums.ErrorType;
import com.okstatelibrary.oralhistoryworkflowcms.service.*;
import com.okstatelibrary.oralhistoryworkflowcms.util.DateUtil;

@Controller
@RequestMapping("subseries")
public class SubserieController extends MainController {

	@Autowired
	private UserService userService;

	@Autowired
	private SubserieService subserieService;

	@Autowired
	private CollectionService collectionService;

	@RequestMapping("/all")
	public String subseries(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		List<Subserie> subseries = subserieService.findAll();

		model.addAttribute("subseries", subseries);

		this.messageSetup(model);

		return "subseries";
	}

	@RequestMapping(value = { "/add" })
	public String add(@PathVariable(required = false) String id, Principal principal, Model model) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		Subserie subserie = new Subserie(Integer.parseInt("0"));

		model.addAttribute("collections", collectionService.findAll());
		model.addAttribute("subseriedetails", subserie);

		return "subseriedetails";
	}

	@RequestMapping(value = { "/{id}" })
	public String details(@PathVariable(required = false) String id, Principal principal, Model model) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		Subserie subserie = null;

		subserie = subserieService.findById(Integer.parseInt(id)).orElse(null);

		model.addAttribute("collections", collectionService.findAll());
		model.addAttribute("subseriedetails", subserie);

		return "subseriedetails";
	}

	@RequestMapping(value = "/getSubseries", method = RequestMethod.GET)
	public @ResponseBody List<Subserie> getSubseries(@RequestParam(value = "collectionId") short collectionId) {

		List<Subserie> list = subserieService.findAllbyCollectionId(collectionId); // .findAll();//

		return list;
	}

	@PostMapping("/update")
	public String update(@ModelAttribute("repository") Subserie subserie, Model model, Principal principal) {

		User user = userService.findByUsername(principal.getName());

		Subserie newSubserie = null;

		if (subserie.getId() > 0) {

			newSubserie = subserieService.findById(subserie.getId()).orElse(null);

			newSubserie.setCollectionId(subserie.getCollectionId());
			newSubserie.setSubseriesName(subserie.getSubseriesName());

		} else {
			newSubserie = subserie;

			newSubserie.setCreatedBy(Integer.parseInt(user.getUserId().toString()));
			newSubserie.setCreatedDate(DateUtil.getTodaySqlDate());
		}

		newSubserie.setUpdatedBy(Integer.parseInt(user.getUserId().toString()));
		newSubserie.setUpdatedDate(DateUtil.getTodaySqlDate());

		Subserie sub = subserieService.saveSubseries(newSubserie);

		if (sub != null && sub.getId() > 0) {

			this.errorType = ErrorType.SUCESS;
			this.message = "Subseries details changed successfully!";

		} else {
			this.errorType = ErrorType.ERROR;
			this.message = "Failed to add subseries!";
		}

		return "redirect:/subseries/all";
	}

	@PostMapping("/delete")
	public String delete(@ModelAttribute("repository") Repository newRepository, Model model) {

		List<Collection> collections = collectionService.findByRepositoryId(newRepository.getId());

		if (collections != null && collections.size() > 0) {

			this.errorType = ErrorType.WARNING;
			this.message = "Cannot delete repository record, due to references!";
		} else {
			subserieService.deleteRepository(newRepository.getId());

			this.errorType = ErrorType.SUCESS;
			this.message = "Successfully deleted repository!";
		}

		return "redirect:/repository/all";
	}
}