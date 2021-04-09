package com.okstatelibrary.oralhistoryworkflowcms.controller;

import java.security.Principal;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.okstatelibrary.oralhistoryworkflowcms.entity.User;
import com.okstatelibrary.oralhistoryworkflowcms.enums.ErrorType;
import com.okstatelibrary.oralhistoryworkflowcms.service.UserService;

@Controller
@RequestMapping("")
public class MainController {

	//@Autowired
	//private UserService userService;
	
	protected String message = "";

	protected ErrorType errorType;

	protected void messageSetup(Model model) {
		if (!message.equals(null) || !message.equals("")) {

			model.addAttribute("errorMessage", message);

			if (errorType != null) {
				switch (errorType) {
				case ERROR:
					model.addAttribute("errortypeclass", "message-error");
					break;
				case WARNING:
					model.addAttribute("errortypeclass", "message-warning");
					break;
				case SUCESS:
					model.addAttribute("errortypeclass", "message-sucess");
					break;
				default:
					model.addAttribute("errortypeclass", "message-sucess");
				}
			}

			model.addAttribute("errorMessage", message);

			message = "";
		}
	}

//	@RequestMapping("/validate")
//	public String validate(Model model, Principal principal) throws ParseException {
//
//		User user = userService.findByUsername(principal.getName());
//		model.addAttribute("user", user);
//
//		this.messageSetup(model);
//
//		return "validate";
//	}

}