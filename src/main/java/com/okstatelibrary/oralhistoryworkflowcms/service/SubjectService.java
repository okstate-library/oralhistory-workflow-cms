package com.okstatelibrary.oralhistoryworkflowcms.service;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectService {

	List<Subject> findAll();
	
	Optional<Subject> findById(short id);
	
	Subject saveSubject(Subject subject);
}