package com.okstatelibrary.oralhistoryworkflowcms.controller;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.okstatelibrary.oralhistoryworkflowcms.entity.*;
import com.okstatelibrary.oralhistoryworkflowcms.enums.ErrorType;
import com.okstatelibrary.oralhistoryworkflowcms.service.*;
import com.okstatelibrary.oralhistoryworkflowcms.util.DateUtil;
import com.okstatelibrary.oralhistoryworkflowcms.util.ExcelService;

@Controller
@RequestMapping("series")
public class SeriesController extends MainController {

	@Autowired
	private UserService userService;

	@Autowired
	private SeriesService seriesService;

	@Autowired
	private CollectionService collectionService;

	@Autowired
	private TranscriptionService transcriptionService;

	@RequestMapping("")
	public String series(Model model, Principal principal) throws ParseException {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		List<Series> series = seriesService.findAll();

		model.addAttribute("series", series);

		this.messageSetup(model);

		return "series";
	}

	@RequestMapping(value = { "/add" })
	public String add(@PathVariable(required = false) String id, Principal principal, Model model) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		Series aseries = new Series(Integer.parseInt("0"));

		model.addAttribute("collections", collectionService.findAll());
		model.addAttribute("aseriesdetails", aseries);

		return "seriesdetails";
	}

	@RequestMapping(value = { "/{id}" })
	public String details(@PathVariable(required = false) String id, Principal principal, Model model) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		Series series = null;

		series = seriesService.findById(Integer.parseInt(id)).orElse(null);

		model.addAttribute("collections", collectionService.findAll());
		model.addAttribute("aseriesdetails", series);

		return "seriesdetails";
	}

	@RequestMapping(value = "/getSeries", method = RequestMethod.GET)
	public @ResponseBody List<Series> getSeries(@RequestParam(value = "collectionId") short collectionId) {

		if (collectionId == 0) {
			return seriesService.findAll();//

		} else {
			return seriesService.findAllbyCollectionId(collectionId); // .findAll();//
		}

	}

	@PostMapping("/update")
	public String update(@ModelAttribute("series") Series series, Model model, Principal principal) {

		User user = userService.findByUsername(principal.getName());

		Series newSeries = null;

		if (series.getId() > 0) {

			newSeries = series;

			Series oldSeries = seriesService.findById(series.getId()).orElse(null);

			newSeries.setCreatedBy(oldSeries.getCreatedBy());
			newSeries.setCreatedDate(oldSeries.getCreatedDate());

		} else {
			newSeries = series;

			newSeries.setCreatedBy(Integer.parseInt(user.getUserId().toString()));
			newSeries.setCreatedDate(DateUtil.getTodaySqlDate());
		}

		newSeries.setUpdatedBy(Integer.parseInt(user.getUserId().toString()));
		newSeries.setUpdatedDate(DateUtil.getTodaySqlDate());

		Series sub = seriesService.saveSeries(newSeries);

		if (sub != null && sub.getId() > 0) {

			this.errorType = ErrorType.SUCESS;
			this.message = "Series details changed successfully!";

		} else {
			this.errorType = ErrorType.ERROR;
			this.message = "Failed to add series!";
		}

		return "redirect:/series";
	}

	@PostMapping("/delete")
	public String delete(@ModelAttribute("series") Series series, Model model) {

		List<Transcription> transcriptions = transcriptionService.findBySeriesId(series.getId());

		if (transcriptions != null && transcriptions.size() > 0) {

			this.errorType = ErrorType.WARNING;
			this.message = "Cannot delete series record, due to references!";
		} else {

			seriesService.deleteSeries(series.getId());

			this.errorType = ErrorType.SUCESS;
			this.message = "Successfully deleted series!";
		}

		return "redirect:/series";
	}

	@PostMapping("/export")
	public ResponseEntity<Resource> export(Model model, HttpServletResponse response) throws IOException {

		String filename = "series.xlsx";

		InputStreamResource file = new InputStreamResource(ExcelService.seriesToExcel(seriesService.findAll()));

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);

	}

	@RequestMapping(value = "/validate", method = RequestMethod.POST)
	public String validate(Model model, HttpServletRequest request, Principal principal) throws IOException {

		User user = userService.findByUsername(principal.getName());

		List<Series> seriesList = seriesService.findAll();

		for (Series aSeries : seriesList) {

			List<Transcription> transcriptions = transcriptionService.findBySeriesId(aSeries.getId());

			aSeries.setNumberOfInterviews(((short) transcriptions.size()));
			aSeries.setUpdatedBy(Integer.parseInt(user.getUserId().toString()));
			aSeries.setUpdatedDate(DateUtil.getTodaySqlDate());

			seriesService.saveSeries(aSeries);
		}

		this.errorType = ErrorType.SUCESS;
		this.message = "Series records validated!";

		return "redirect:/validate";
	}
}