package com.okstatelibrary.oralhistoryworkflowcms.service;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Series;

import java.util.List;
import java.util.Optional;

public interface SeriesService {

	List<Series> findAll();

	List<Series> findAllbyCollectionId(short collectionId);
	
	Optional<Series> findById(int Id);
	
	Series saveSeries(Series aseries);

	void deleteSeries(int seriesId);

	Series findByName(short collectionId, String seriesName);

	List<Series> findAll(List<Short> ids);
}