package com.okstatelibrary.oralhistoryworkflowcms.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.okstatelibrary.oralhistoryworkflowcms.entity.PredefinedUser;
import com.okstatelibrary.oralhistoryworkflowcms.entity.Subserie;
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

	@Override
	public List<PredefinedUser> findAllByType(short type) {
		return this.findAll().stream().filter(u -> u.getUserType() == type).collect(Collectors.toList());
	}

	@Cacheable(cacheNames = "findByPredefinedUserIdCache")
	@Override
	public Optional<PredefinedUser> findById(short Id) {
		return predefinedUserRepository.findById(Id);
	}

	@Caching(evict = { @CacheEvict(value = "findAllPredefinedUserCache", allEntries = true),
			@CacheEvict(value = "findByPredefinedUserIdCache", allEntries = true) })
	public PredefinedUser savePredefinedUser(PredefinedUser predefinedUser) {

		return predefinedUserRepository.save(predefinedUser);

//		if (predefinedUser.getId() == 0) {
//			
//		} else {
//			if (findById(predefinedUser.getId()) != null) {
//				return predefinedUserRepository.save(predefinedUser);
//			} else {
//				return null;
//			}
//		}
	}

	public void flushCache() {
		for (String cacheName : cacheManager.getCacheNames()) {
			cacheManager.getCache(cacheName).clear();
			// LOG.info("Flushing cache with name: " + cacheName);
		}
	}

	@Override
	public void deletePredefinedUser(short id) {
		predefinedUserRepository.deleteById(id);
	}

	@Override
	public List<PredefinedUser> findAllByType(short[] ids) {
		
		List<PredefinedUser> predefineUsers =  new ArrayList<PredefinedUser>();
	
		for (Short id : ids) {
			predefineUsers.add(findById(id).orElse(null));
		}
		
		return predefineUsers;
	
	}

}