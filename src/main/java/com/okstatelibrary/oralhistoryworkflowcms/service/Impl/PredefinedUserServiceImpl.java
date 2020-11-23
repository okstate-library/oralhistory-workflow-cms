package com.okstatelibrary.oralhistoryworkflowcms.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.okstatelibrary.oralhistoryworkflowcms.entity.PredefinedUser;
import com.okstatelibrary.oralhistoryworkflowcms.repository.PredefineRepository;
import com.okstatelibrary.oralhistoryworkflowcms.service.PredefinedUserService;

import org.springframework.cache.annotation.*;

@Service
public class PredefinedUserServiceImpl implements PredefinedUserService {

	@Autowired
	private PredefineRepository predefinedUserRepository;

	@Autowired
	CacheManager cacheManager;

	@Cacheable(cacheNames = "findAllPredefinedUserCache")
	@Override
	public List<PredefinedUser> findAll() {
		return predefinedUserRepository.findAll();
	}

	@Cacheable(cacheNames = "findByPredefinedUserIdCache")
	@Override
	public Optional<PredefinedUser> findById(short Id) {
		return predefinedUserRepository.findById(Id);
	}

	@Caching(evict = { @CacheEvict(value = "findAllPredefinedUserCache", allEntries = true),
			@CacheEvict(value = "findByPredefinedUserIdCache", allEntries = true) })
	public PredefinedUser savePredefinedUser(PredefinedUser predefinedUser) {
		
		if (predefinedUser.getId() == 0) {
			return predefinedUserRepository.save(predefinedUser);
		} else {
			if (findById(predefinedUser.getId()) != null) {
				return predefinedUserRepository.save(predefinedUser);
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