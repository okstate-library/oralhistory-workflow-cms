package com.okstatelibrary.oralhistoryworkflowcms.apiresource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Transcription;
import com.okstatelibrary.oralhistoryworkflowcms.service.TranscriptionService;

@RestController
@RequestMapping("/api")
public class ApiResource {

	@Autowired
	private TranscriptionService transcriptionService;

	@RequestMapping(value = "/interviews", method = RequestMethod.GET)
	public Interviews interviews() {

		return new Interviews(transcriptionService.findAll(true));

	}

	@RequestMapping(value = "/interview/{id}", method = RequestMethod.GET)
	public Interview interview(@PathVariable int id) {

		return new Interview(transcriptionService.findById(id));

	}

	public class Interviews {

		public Interviews(List<Transcription> list) {
			this.list = list;
		}

		private List<Transcription> list;

		public List<Transcription> getList() {
			return list;
		}

		public void setList(List<Transcription> list) {
			this.list = list;
		}

	}

	public class Interview {

		public Interview(Optional<Transcription> optional) {
			this.interview = optional;
		}

		private Optional<Transcription> interview;

		public Optional<Transcription> getInterview() {
			return interview;
		}

		public void setInterview(Optional<Transcription> interview) {
			this.interview = interview;
		}

	}

}