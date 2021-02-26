package com.okstatelibrary.oralhistoryworkflowcms.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Collection;
import com.okstatelibrary.oralhistoryworkflowcms.entity.Subserie;
import com.okstatelibrary.oralhistoryworkflowcms.repository.SubserieRepository;
import com.okstatelibrary.oralhistoryworkflowcms.service.SubserieService;

import org.springframework.cache.annotation.*;

@Service
public class SubserieServiceImpl implements SubserieService {

	@Autowired
	private SubserieRepository subserieRepository;

	@Autowired
	CacheManager cacheManager;

	@Cacheable(cacheNames = "findAllSubseriesCache")
	@Override
	public List<Subserie> findAll() {
		return subserieRepository.findAll();
	}

	@Override
	public List<Subserie> findAll(short[] ids) {
		List<Subserie> subseriesList = new ArrayList<Subserie>();

		for (Short id : ids) {
			subseriesList.add(findById(id).orElse(null));
		}

		return subseriesList;
	}
	
	@Cacheable(cacheNames = "findBySubseriesIdCache")
	@Override
	public Optional<Subserie> findById(int Id) {
		return subserieRepository.findById(Id);
	}

	@Caching(evict = { @CacheEvict(value = "findAllSubseriesCache", allEntries = true),
			@CacheEvict(value = "findBySubseriesIdCache", allEntries = true) })
	public Subserie saveSubseries(Subserie subseries) {
		if (subseries.getId() == 0) {
			return subserieRepository.save(subseries);
		} else {
			if (findById(subseries.getId()) != null) {
				return subserieRepository.save(subseries);
			} else {
				return null;
			}
		}
	}

	@Override
	public List<Subserie> findAllbyCollectionId(short collectionId) {

		System.out.println("Collection id " + collectionId);

		List<Subserie> list = null;

		list = subserieRepository.findAll().stream().filter(u -> u.getCollection().getId() == collectionId)
				.collect(Collectors.toList());
		return list;

	}

	public void flushCache() {
		for (String cacheName : cacheManager.getCacheNames()) {
			cacheManager.getCache(cacheName).clear();
			// LOG.info("Flushing cache with name: " + cacheName);
		}
	}

	@Override
	public void deleteSubseries(int subserieId) {
		subserieRepository.deleteById(subserieId);
	}

	@Override
	public Subserie findByName(short collectionId, String subseriesName) {

		if (subseriesName != null && !subseriesName.isEmpty()) {
			return findAll().stream()
					.filter(u -> u.getSubseriesName().toLowerCase().contains(subseriesName.toLowerCase())
							&& u.getCollectionId() == collectionId)
					.findFirst().get();
		} else {
			return findAll().stream().filter(u -> u.getCollectionId() == collectionId).findFirst().get();
		}

	}

	
}