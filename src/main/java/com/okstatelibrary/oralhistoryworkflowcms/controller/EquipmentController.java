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
@RequestMapping("equipments")
public class EquipmentController extends MainController {

	@Autowired
	private UserService userService;

	@Autowired
	private EquipmentService equipmentService;

	@Autowired
	private TranscriptionService transcriptionService;

	@RequestMapping("")
	public String equipments(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		model.addAttribute("equipments", equipmentService.findAll());

		this.messageSetup(model);

		return "equipments";
	}

	@RequestMapping(value = { "/add" })
	public String add(@PathVariable(required = false) String id, Principal principal, Model model) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		Equipments equipment = new Equipments(Short.parseShort("0"));

		model.addAttribute("equipmentdetails", equipment);

		return "equipmentdetails";
	}

	@RequestMapping(value = { "/{id}" })
	public String details(@PathVariable(required = false) String id, Principal principal, Model model) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		Equipments equipment = equipmentService.findById(Short.parseShort(id)).orElse(null);

		model.addAttribute("equipmentdetails", equipment);

		return "equipmentdetails";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute("equipment") Equipments equipment, Model model, Principal principal) {

		Equipments nEquipment = equipmentService.saveEquipment(equipment);

		if (nEquipment != null && nEquipment.getId() > 0) {

			this.errorType = ErrorType.SUCESS;
			this.message = "Equipment details changed successfully!";

		} else {
			this.errorType = ErrorType.ERROR;
			this.message = "Failed to add collection!";
		}

		return "redirect:/equipments";
	}

	@PostMapping("/delete")
	public String delete(@ModelAttribute("equipment") Equipments equipment, Model model) {

		if (transcriptionService.isRecordsExistsByEquipmentName(equipment.getEquipmentUsedName())) {

			this.errorType = ErrorType.WARNING;
			this.message = "Cannot delete equipment record, due to references!";
		} else {

			equipmentService.deleteEquipment(equipment.getId());

			this.errorType = ErrorType.SUCESS;
			this.message = "Successfully deleted equipment record!";
		}

		return "redirect:/equipments";
	}

}