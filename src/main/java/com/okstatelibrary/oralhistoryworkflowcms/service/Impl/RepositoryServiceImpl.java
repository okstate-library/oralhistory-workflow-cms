package com.okstatelibrary.oralhistoryworkflowcms.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Repository;
import com.okstatelibrary.oralhistoryworkflowcms.repository.RepositoryRepository;
import com.okstatelibrary.oralhistoryworkflowcms.service.RepositoryService;

import org.springframework.cache.annotation.*;

@Service
public class RepositoryServiceImpl implements RepositoryService {

	@Autowired
	private RepositoryRepository repositoryRepository;

	@Autowired
	CacheManager cacheManager;

	@Cacheable(cacheNames = "findAllRepositoryCache")
	@Override
	public List<Repository> findAll() {
		return repositoryRepository.findAll();
	}

	@Cacheable(cacheNames = "findByRepositoryIdCache")
	@Override
	public Optional<Repository> findById(short Id) {
		return repositoryRepository.findById(Id);
	}

	@Caching(evict = { @CacheEvict(value = "findAllRepositoryCache", allEntries = true),
			@CacheEvict(value = "findByRepositoryIdCache", allEntries = true) })
	public Repository saveRepository(Repository repository) {
		if (repository.getId() == 0) {
			return repositoryRepository.save(repository);
		} else {
			if (findById(repository.getId()) != null) {
				return repositoryRepository.save(repository);
			} else {
				return null;
			}
		}
	}

	public void flushCache() {
		for (String cacheName : cacheManager.getCacheNames()) {
			cacheManager.getCache(cacheName).clear();
			System.out.print("Flushing cache with name: " + cacheName);
		}
	}

	@Override
	public void deleteRepository(short Id) {
		repositoryRepository.deleteById(Id);
		
		flushCache() ;
	}
}