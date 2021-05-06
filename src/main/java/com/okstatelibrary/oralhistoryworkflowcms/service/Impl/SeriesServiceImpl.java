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

import com.okstatelibrary.oralhistoryworkflowcms.entity.Series;
import com.okstatelibrary.oralhistoryworkflowcms.repository.SeriesRepository;
import com.okstatelibrary.oralhistoryworkflowcms.service.SeriesService;

import org.springframework.cache.annotation.*;

@Service
public class SeriesServiceImpl implements SeriesService {

	@Autowired
	private SeriesRepository seriesRepository;

	@Autowired
	CacheManager cacheManager;

	@Cacheable(cacheNames = "findAllSeriesCache")
	@Override
	public List<Series> findAll() {

		return seriesRepository.findAll().stream().sorted(Comparator.comparing(Series::getSeriesName))
				.collect(Collectors.toList());
	}

	@Override
	public List<Series> findAll(List<Short> ids) {
		List<Series> seriesList = new ArrayList<Series>();

		for (Short id : ids) {
			seriesList.add(findById(id).orElse(null));
		}

		return seriesList.stream().sorted(Comparator.comparing(Series::getSeriesName))
				.collect(Collectors.toList());
	}

	@Cacheable(cacheNames = "findBySeriesIdCache")
	@Override
	public Optional<Series> findById(int Id) {
		return seriesRepository.findById(Id);
	}

	@Caching(evict = { @CacheEvict(value = "findAllSeriesCache", allEntries = true),
			@CacheEvict(value = "findBySeriesIdCache", allEntries = true) })
	public Series saveSeries(Series series) {
		if (series.getId() == 0) {
			return seriesRepository.save(series);
		} else {
			if (findById(series.getId()) != null) {
				return seriesRepository.save(series);
			} else {
				return null;
			}
		}
	}

	@Override
	public List<Series> findAllbyCollectionId(short collectionId) {

		List<Series> list = null;

		list = seriesRepository.findAll().stream().sorted(Comparator.comparing(Series::getSeriesName))
				.filter(u -> u.getCollection().getId() == collectionId).collect(Collectors.toList());

		return list;

	}

	public void flushCache() {
		for (String cacheName : cacheManager.getCacheNames()) {
			cacheManager.getCache(cacheName).clear();
			// LOG.info("Flushing cache with name: " + cacheName);
		}
	}

	@Override
	public void deleteSeries(int serieId) {
		seriesRepository.deleteById(serieId);
	}

	@Override
	public Series findByName(short collectionId, String seriesName) {

		if (seriesName != null && !seriesName.isEmpty()) {
			return findAll().stream()
					.filter(u -> u.getSeriesName().toLowerCase().contains(seriesName.toLowerCase())
							&& u.getCollectionId() == collectionId)
					.findFirst().get();
		} else {
			return findAll().stream().filter(u -> u.getCollectionId() == collectionId).findFirst().get();
		}

	}

}