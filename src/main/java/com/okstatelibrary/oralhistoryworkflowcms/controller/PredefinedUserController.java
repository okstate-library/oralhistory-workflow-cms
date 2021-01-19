package com.okstatelibrary.oralhistoryworkflowcms.controller;

import java.security.Principal;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.okstatelibrary.oralhistoryworkflowcms.entity.*;
import com.okstatelibrary.oralhistoryworkflowcms.enums.PredefinedUserType;
import com.okstatelibrary.oralhistoryworkflowcms.service.*;

@Controller
@RequestMapping("")
public class PredefinedUserController extends MainController {

	@Autowired
	private UserService userService;

	@Autowired
	private PredefinedUserService predefinedUserService;

	@RequestMapping("/interviewers")
	public String interviewers(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		//@SuppressWarnings("unchecked")
		List<PredefinedUser> interviewers = predefinedUserService.findAllByType(PredefinedUserType.TRANSCRIBER.getCode());
		
		System.out.println("interviewers length " +  interviewers.size());
		
		model.addAttribute("interviewers", interviewers);
		
		this.messageSetup(model);

		return "interviewers";
	}
//
//	@RequestMapping(value = { "/repository", "/repository/{id}" })
//	public String details(@PathVariable(required = false) String id, Principal principal, Model model) {
//
//		User user = userService.findByUsername(principal.getName());
//		model.addAttribute("user", user);
//
//		Repository repo = null;
//
//		if (id != null && !id.isEmpty() && !id.isBlank()) {
//			repo = repositoryService.findById(Short.parseShort(id)).orElse(null);
//
//			model.addAttribute("repositorydetails", repo);
//		} else {
//			repo = new Repository(Short.parseShort("0"), "");
//		}
//
//		model.addAttribute("repositorydetails", repo);
//
//		return "repositorydetails";
//	}
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