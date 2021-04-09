package com.okstatelibrary.oralhistoryworkflowcms.service.Impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
		return collectionRepository.findAll().stream().sorted(Comparator.comparing(Collection::getCollectionName))
				.collect(Collectors.toList());

	}

	@Override
	public List<Collection> findAll(short[] ids) {

		List<Collection> collections = new ArrayList<Collection>();

		for (Short id : ids) {
			collections.add(findById(id).orElse(null));
		}

		return collections.stream().sorted(Comparator.comparing(Collection::getCollectionName))
				.collect(Collectors.toList());
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

	@Override
	public List<Collection> findByRepositoryId(short RepositoryId) {
		return findAll().stream().sorted(Comparator.comparing(Collection::getCollectionName))
				.filter(u -> u.getRepositoryId() == RepositoryId).collect(Collectors.toList())

		;
	}

	@Override
	public void deleteCollection(short Id) {
		collectionRepository.deleteById(Id);

		flushCache();
	}

	@Override
	public Collection findByName(String collectionName) {

		List<Collection> collections = findAll().stream()
				.filter(u -> u.getCollectionName().toLowerCase().contains(collectionName.toLowerCase()))
				.collect(Collectors.toList());

		if (collections != null && collections.size() > 0) {
			return collections.get(0);
		}

		return null;
	}

}