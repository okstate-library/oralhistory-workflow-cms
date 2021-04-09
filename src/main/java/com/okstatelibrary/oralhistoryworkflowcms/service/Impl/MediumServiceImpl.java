package com.okstatelibrary.oralhistoryworkflowcms.service.Impl;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Medium;
import com.okstatelibrary.oralhistoryworkflowcms.repository.MediumRepository;
import com.okstatelibrary.oralhistoryworkflowcms.service.MediumService;

@Service
public class MediumServiceImpl implements MediumService {

	@Autowired
	private MediumRepository mediumRepository;

	@Autowired
	CacheManager cacheManager;

	@Cacheable(cacheNames = "findAllMediumCache")
	@Override
	public List<Medium> findAll() {
		return mediumRepository.findAll().stream().sorted(Comparator.comparing(Medium::getMediumName))
				.collect(Collectors.toList());
	}

	public void flushCache() {
		for (String cacheName : cacheManager.getCacheNames()) {
			cacheManager.getCache(cacheName).clear();
		}
	}

	@Override
	public Medium saveEquipment(Medium equipment) {

		flushCache();

		return mediumRepository.save(equipment);

	}

	@Override
	public Optional<Medium> findById(short Id) {
		return mediumRepository.findById(Id);
	}

	@Override
	public void deleteMedium(short id) {
		mediumRepository.deleteById(id);

		flushCache();
	}

}