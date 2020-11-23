package com.okstatelibrary.oralhistoryworkflowcms.service;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Collection;
import com.okstatelibrary.oralhistoryworkflowcms.entity.Subserie;

import java.util.List;
import java.util.Optional;

public interface SubserieService {

	List<Subserie> findAll();

	List<Subserie> findAllbyCollectionId(short collectionId);
	
	Optional<Subserie> findById(int Id);
	
	Subserie saveSubseries(Subserie subserie);
}