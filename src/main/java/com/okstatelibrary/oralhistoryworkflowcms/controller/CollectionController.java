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
public class CollectionController extends MainController {

	@Autowired
	private UserService userService;

	@Autowired
	private CollectionService collectionService;

	@RequestMapping("/collections")
	public String collections(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		List<Collection> collections = collectionService.findAll();
		
//		for (Collection collection: collections)
//		{
//			Sysstem.out.println();
//		}
		model.addAttribute("collections", collections);

//		Collection col= null;
//		col.getRepository().getRepositoryName();
		
		
		this.messageSetup(model);

		return "collections";
	}

	@RequestMapping(value = { "/collection", "/collection/{id}" })
	public String details(@PathVariable(required = false) String id, Principal principal, Model model) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		Collection collection = null;

		if (id != null && !id.isEmpty()) {
			collection = collectionService.findById(Short.parseShort(id)).orElse(null);

			model.addAttribute("collectiondetails", collection);
		} else {
			collection = new Collection(Short.parseShort("0"));
		}

		model.addAttribute("collectiondetails", collection);

		return "collectiondetails";
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