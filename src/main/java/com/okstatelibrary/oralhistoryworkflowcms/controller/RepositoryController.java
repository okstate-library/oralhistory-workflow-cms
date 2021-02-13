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
@RequestMapping("repositories")
public class RepositoryController extends MainController {

	@Autowired
	private UserService userService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private CollectionService collectionService;

	@RequestMapping("")
	public String all(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		model.addAttribute("repositories", repositoryService.findAll());

		this.messageSetup(model);

		return "repositories";
	}

	@RequestMapping(value = { "/add" })
	public String add(@PathVariable(required = false) String id, Principal principal, Model model) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		Repository repo = new Repository(Short.parseShort("0"), "");

		model.addAttribute("repositorydetails", repo);

		return "repositorydetails";
	}

	@RequestMapping(value = { "/{id}" })
	public String details(@PathVariable(required = false) String id, Principal principal, Model model) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		Repository repo = repositoryService.findById(Short.parseShort(id)).orElse(null);

		model.addAttribute("repositorydetails", repo);

		return "repositorydetails";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute("repository") Repository newRepository, Model model) {

		Repository repo = repositoryService.saveRepository(newRepository);

		if (repo != null && repo.getId() > 0) {

			this.errorType = ErrorType.SUCESS;
			this.message = "Repository details changed successfully!";

		} else {
			this.errorType = ErrorType.ERROR;
			this.message = "Failed to add repository details!";
		}

		return "redirect:/repositories";
	}

	@PostMapping("/delete")
	public String delete(@ModelAttribute("repository") Repository newRepository, Model model) {

		List<Collection> collections = collectionService.findByRepositoryId(newRepository.getId());

		if (collections != null && collections.size() > 0) {

			this.errorType = ErrorType.WARNING;
			this.message = "Cannot delete repository record, due to references!";
		} else {
			repositoryService.deleteRepository(newRepository.getId());

			this.errorType = ErrorType.SUCESS;
			this.message = "Successfully deleted repository!";
		}

		return "redirect:/repositories";
	}

}