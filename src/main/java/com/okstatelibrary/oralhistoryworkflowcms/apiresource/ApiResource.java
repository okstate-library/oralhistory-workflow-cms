package com.okstatelibrary.oralhistoryworkflowcms.apiresource;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Transcription;
import com.okstatelibrary.oralhistoryworkflowcms.entity.TranscriptionRest;
import com.okstatelibrary.oralhistoryworkflowcms.service.TranscriptionService;

@RestController
@RequestMapping("/api")
public class ApiResource {

	@Autowired
	private TranscriptionService transcriptionService;

	@RequestMapping(value = "/interviews", method = RequestMethod.GET)
	public List<TranscriptionRest> interviews() throws IllegalAccessException, InvocationTargetException {

		List<Transcription> listOrigin = transcriptionService.findPublic();

		List<TranscriptionRest> listDestination = new ArrayList<TranscriptionRest>();

		for (Transcription source : listOrigin) {
			TranscriptionRest target = new TranscriptionRest();
			BeanUtils.copyProperties(target, source);
			listDestination.add(target);
		}

		return listDestination;

	}

	@RequestMapping(value = "/interview/{id}", method = RequestMethod.GET)
	public TranscriptionRest interview(@PathVariable int id) throws IllegalAccessException, InvocationTargetException {

		TranscriptionRest newObject = new TranscriptionRest();

		Transcription transcription = transcriptionService.findById(id).orElse(null);

		if (transcription != null && transcription.getIsPublic() == 1) {
			BeanUtils.copyProperties(newObject, transcription);

		}

		return newObject;

	}

//	public class Interviews {
//
//		public Interviews(List<Transcription> list) {
//			this.list = list;
//		}
//
//		private List<Transcription> list;
//
//		public List<Transcription> getList() {
//			return list;
//		}
//
//		public void setList(List<Transcription> list) {
//			this.list = list;
//		}
//
//	}
//
//	public class Interview {
//
//		public Interview(Optional<Transcription> optional) {
//			this.interview = optional;
//		}
//
//		private Optional<Transcription> interview;
//
//		public Optional<Transcription> getInterview() {
//			return interview;
//		}
//
//		public void setInterview(Optional<Transcription> interview) {
//			this.interview = interview;
//		}
//
//	}

}