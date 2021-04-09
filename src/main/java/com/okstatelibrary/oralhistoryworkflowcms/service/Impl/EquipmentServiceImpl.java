package com.okstatelibrary.oralhistoryworkflowcms.service.Impl;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
		return equipmentUsedRepository.findAll().stream().sorted(Comparator.comparing(Equipments::getEquipmentTypeName))
				.collect(Collectors.toList());
	}

	public void flushCache() {
		for (String cacheName : cacheManager.getCacheNames()) {
			cacheManager.getCache(cacheName).clear();
			// LOG.info("Flushing cache with name: " + cacheName);
		}
	}

	@Override
	public List<Equipments> findAllByType(byte type) {
		return this.findAll().stream().filter(e -> e.getEquipmentType() == type).collect(Collectors.toList());
	}

	@Override
	public Equipments saveEquipment(Equipments equipment) {
		flushCache();
		return equipmentUsedRepository.save(equipment);
	}

	@Override
	public Optional<Equipments> findById(short Id) {
		return equipmentUsedRepository.findById(Id);
	}

	@Override
	public void deleteEquipment(short id) {
		equipmentUsedRepository.deleteById(id);

		flushCache();
	}

}