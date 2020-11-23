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
import com.okstatelibrary.oralhistoryworkflowcms.entity.*;
import com.okstatelibrary.oralhistoryworkflowcms.service.*;

@Controller
@RequestMapping("")
public class SubserieController extends MainController {

	@Autowired
	private UserService userService;

	@Autowired
	private SubserieService subserieService;

	@RequestMapping("/subseries")
	public String subseries(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		List<Subserie> subseries = subserieService.findAll();
		
//		for (Collection collection: collections)
//		{
//			Sysstem.out.println();
//		}
		model.addAttribute("subseries", subseries);

//		Collection col= null;
//		col.getRepository().getRepositoryName();
		
		
		
		this.messageSetup(model);

		return "subseries";
	}

	@RequestMapping(value = { "/subserie", "/subserie/{id}" })
	public String details(@PathVariable(required = false) String id, Principal principal, Model model) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		Subserie subserie = null;

		if (id != null && !id.isEmpty() && !id.isBlank()) {
			subserie = subserieService.findById(Integer.parseInt(id)).orElse(null);

			model.addAttribute("subseriedetails", subserie);
		} else {
			subserie = new Subserie(Integer.parseInt("0"));
		}

		model.addAttribute("subseriedetails", subserie);

		return "subseriedetails";
	}
//
//	@PostMapping("/repository/update")
//	public String update(@ModelAttribute("repository") Repository newRepository, Model model) {
//
//		
////		if (newRepository.getId() > 0)
////		{
//			repositoryService.saveRepository(newRepository);
////		}
////		else
////		{
////			
////		}
//		
//		// Repository repo =
//		// repositoryService.findById(newRepository.getId()).orElse(null);
//
//		System.out.println(newRepository.getRepositoryName());
////		
////		User user = userService.findByUsername(newUser.getUsername());
////		
////		user.setUsername(newUser.getUsername());
////		user.setFirstName(newUser.getFirstName());
////		user.setLastName(newUser.getLastName());
////		user.setEmail(newUser.getEmail());
////
////		model.addAttribute("user", user);
////
////		userService.saveUser(user);
////
//		this.message = "Repossitory details changed successfully!";
//
//		return "redirect:/repositories";
//	}

}