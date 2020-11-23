package com.okstatelibrary.oralhistoryworkflowcms.service;

import com.okstatelibrary.oralhistoryworkflowcms.entity.PredefinedUser;

import java.util.List;
import java.util.Optional;

public interface PredefinedUserService {

	List<PredefinedUser> findAll();
	
	Optional<PredefinedUser> findById(short Id);
	
	PredefinedUser savePredefinedUser(PredefinedUser collection);
}