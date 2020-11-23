package com.okstatelibrary.oralhistoryworkflowcms.service;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Collection;

import java.util.List;
import java.util.Optional;

public interface CollectionService {

	List<Collection> findAll();
	
	Optional<Collection> findById(short Id);
	
	Collection saveCollection(Collection collection);
}