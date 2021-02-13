package com.okstatelibrary.oralhistoryworkflowcms.service;

import com.okstatelibrary.oralhistoryworkflowcms.entity.PredefinedUser;

import java.util.List;
import java.util.Optional;

public interface PredefinedUserService {

	List<PredefinedUser> findAll();
	
	List<PredefinedUser> findAllByType(short type);
	
	Optional<PredefinedUser> findById(short id);
	
	PredefinedUser savePredefinedUser(PredefinedUser collection);

	void deletePredefinedUser(short id);
}