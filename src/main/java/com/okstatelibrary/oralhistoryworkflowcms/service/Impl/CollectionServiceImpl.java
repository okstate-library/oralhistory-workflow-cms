package com.okstatelibrary.oralhistoryworkflowcms.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Collection;
import com.okstatelibrary.oralhistoryworkflowcms.repository.CollectionRepository;
import com.okstatelibrary.oralhistoryworkflowcms.service.CollectionService;
import org.springframework.cache.annotation.*;

@Service
public class CollectionServiceImpl implements CollectionService {

	@Autowired
	private CollectionRepository collectionRepository;

	@Autowired
	CacheManager cacheManager;

	@Cacheable(cacheNames = "findAllCollectionCache")
	@Override
	public List<Collection> findAll() {
		return collectionRepository.findAll();
	}

	@Cacheable(cacheNames = "findByCollectionIdCache")
	@Override
	public Optional<Collection> findById(short Id) {
		return collectionRepository.findById(Id);
	}

	@Caching(evict = { @CacheEvict(value = "findAllCollectionCache", allEntries = true),
			@CacheEvict(value = "findByCollectionIdCache", allEntries = true) })
	public Collection saveCollection(Collection repository) {
		if (repository.getId() == 0) {
			return collectionRepository.save(repository);
		} else {
			if (findById(repository.getId()) != null) {
				return collectionRepository.save(repository);
			} else {
				return null;
			}
		}
	}

	public void flushCache() {
		for (String cacheName : cacheManager.getCacheNames()) {
			cacheManager.getCache(cacheName).clear();
			// LOG.info("Flushing cache with name: " + cacheName);
		}
	}
}