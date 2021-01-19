package com.okstatelibrary.oralhistoryworkflowcms.service;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Repository;

import java.util.List;
import java.util.Optional;

public interface RepositoryService {

	List<Repository> findAll();
	
	Optional<Repository> findById(short Id);
	
	Repository saveRepository(Repository repository);
	
	void deleteRepository(short Id);
}