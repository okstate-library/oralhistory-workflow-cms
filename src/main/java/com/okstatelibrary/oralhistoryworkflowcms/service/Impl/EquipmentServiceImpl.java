package com.okstatelibrary.oralhistoryworkflowcms.service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Equipments;
import com.okstatelibrary.oralhistoryworkflowcms.repository.EquipmentRepository;
import com.okstatelibrary.oralhistoryworkflowcms.service.EquipmentService;

@Service
public class EquipmentServiceImpl implements EquipmentService {

	@Autowired
	private EquipmentRepository equipmentUsedRepository;

	@Autowired
	CacheManager cacheManager;

	@Cacheable(cacheNames = "findAllEquipmentUsedCache")
	@Override
	public List<Equipments> findAll() {
		return equipmentUsedRepository.findAll();
	}

	public void flushCache() {
		for (String cacheName : cacheManager.getCacheNames()) {
			cacheManager.getCache(cacheName).clear();
			// LOG.info("Flushing cache with name: " + cacheName);
		}
	}

	@Override
	public List<Equipments> findAllAudios() {
		return this.findAll().stream().filter(e -> e.getEquipmentType() == (byte) 1).collect(Collectors.toList());
	}

	@Override
	public List<Equipments> findAllVideos() {
		// TODO Auto-generated method stub
		return this.findAll().stream().filter(e -> e.getEquipmentType() == (byte) 2).collect(Collectors.toList());
	}
}