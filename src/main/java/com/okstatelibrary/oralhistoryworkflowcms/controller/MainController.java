package com.okstatelibrary.oralhistoryworkflowcms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class MainController {

	protected String message = "";

	protected void messageSetup(Model model) {
		if (!message.equals(null) || !message.equals("")) {

			model.addAttribute("errorMessage", message);

			message = "";
		}
	}
}