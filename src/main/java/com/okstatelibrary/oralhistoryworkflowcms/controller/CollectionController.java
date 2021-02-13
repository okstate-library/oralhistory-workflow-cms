package com.okstatelibrary.oralhistoryworkflowcms.controller;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.okstatelibrary.oralhistoryworkflowcms.entity.*;
import com.okstatelibrary.oralhistoryworkflowcms.enums.ErrorType;
import com.okstatelibrary.oralhistoryworkflowcms.service.*;
import com.okstatelibrary.oralhistoryworkflowcms.util.DateUtil;
import com.okstatelibrary.oralhistoryworkflowcms.util.ExcelService;

@Controller
@RequestMapping("collections")
public class CollectionController extends MainController {

	@Autowired
	private UserService userService;

	@Autowired
	private CollectionService collectionService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private TranscriptionService trancriptionService;

	@RequestMapping("")
	public String collections(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		List<Collection> collections = collectionService.findAll();

		model.addAttribute("collections", collections);

		this.messageSetup(model);

		return "collections";
	}

	@RequestMapping(value = { "/add" })
	public String add(@PathVariable(required = false) String id, Principal principal, Model model) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		Collection collection = new Collection(Short.parseShort("0"));

		model.addAttribute("repositories", repositoryService.findAll());
		model.addAttribute("collectiondetails", collection);

		return "collectiondetails";
	}

	@RequestMapping(value = { "/{id}" })
	public String details(@PathVariable(required = false) String id, Principal principal, Model model) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		Collection collection = collectionService.findById(Short.parseShort(id)).orElse(null);

		model.addAttribute("collectiondetails", collection);

		model.addAttribute("repositories", repositoryService.findAll());
		model.addAttribute("collectiondetails", collection);

		return "collectiondetails";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute("collection") Collection collection, Model model, Principal principal) {

		User user = userService.findByUsername(principal.getName());

		Collection newCollection = null;

		if (collection.getId() > 0) {

			newCollection = collectionService.findById(collection.getId()).orElse(null);

			newCollection.setRepositoryId(collection.getRepositoryId());
			newCollection.setCollectionName(collection.getCollectionName());

		} else {
			newCollection = collection;

			newCollection.setCreatedBy(Integer.parseInt(user.getUserId().toString()));
			newCollection.setCreatedDate(DateUtil.getTodaySqlDate());
		}

		newCollection.setUpdatedBy(Integer.parseInt(user.getUserId().toString()));
		newCollection.setUpdatedDate(DateUtil.getTodaySqlDate());

		Collection coll = collectionService.saveCollection(newCollection);

		if (coll != null && coll.getId() > 0) {

			this.errorType = ErrorType.SUCESS;
			this.message = "Collection details changed successfully!";

		} else {
			this.errorType = ErrorType.ERROR;
			this.message = "Failed to add collection!";
		}

		return "redirect:/collections";
	}

	@PostMapping("/delete")
	public String delete(@ModelAttribute("collection") Collection collection, Model model) {

		List<Transcription> transcriptions = trancriptionService.findAll(collection.getId() + "");

		if (transcriptions != null && transcriptions.size() > 0) {

			this.errorType = ErrorType.WARNING;
			this.message = "Cannot delete collection record, due to references!";
		} else {
			collectionService.deleteCollection(collection.getId());

			this.errorType = ErrorType.SUCESS;
			this.message = "Successfully deleted collection record!";
		}

		return "redirect:/collections";
	}

	@PostMapping("/export")
	public ResponseEntity<Resource> export(Model model, HttpServletResponse response) throws IOException {

		String filename = "collections.xlsx";

		InputStreamResource file = new InputStreamResource(
				ExcelService.collecctionsToExcel(collectionService.findAll()));

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);

	}

}