package com.okstatelibrary.oralhistoryworkflowcms.service;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Collection;

import java.util.List;
import java.util.Optional;

public interface CollectionService {

	List<Collection> findAll();

	List<Collection> findAll(short[] ids);

	List<Collection> findByRepositoryId(short RepositoryId);

	Optional<Collection> findById(short Id);

	Collection saveCollection(Collection collection);

	void deleteCollection(short Id);

	Collection findByName(String collectionName);
}