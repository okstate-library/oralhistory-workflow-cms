package com.okstatelibrary.oralhistoryworkflowcms.service.Impl;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import com.okstatelibrary.oralhistoryworkflowcms.entity.Subject;
import com.okstatelibrary.oralhistoryworkflowcms.repository.SubjectRepository;
import com.okstatelibrary.oralhistoryworkflowcms.service.SubjectService;

import org.springframework.cache.annotation.*;

@Repository
public class SubjectServiceImpl implements SubjectService {

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	CacheManager cacheManager;

	@Cacheable(cacheNames = "findAllSubjectCache")
	@Override
	public List<Subject> findAll() {
		return subjectRepository.findAll().stream().sorted(Comparator.comparing(Subject::getSubjectName))
				.collect(Collectors.toList());

	}

	@Cacheable(cacheNames = "findBySubjectIdCache")
	@Override
	public Optional<Subject> findById(short Id) {
		return subjectRepository.findById(Id);
	}

	@Caching(evict = { @CacheEvict(value = "findAllSubjectCache", allEntries = true),
			@CacheEvict(value = "findBySubjectIdCache", allEntries = true) })
	public Subject saveSubject(Subject subject) {
		flushCache();

		return subjectRepository.save(subject);

	}

	public void flushCache() {
		for (String cacheName : cacheManager.getCacheNames()) {
			cacheManager.getCache(cacheName).clear();
			// LOG.info("Flushing cache with name: " + cacheName);
		}
	}

	@Override
	public void deleteSubject(short id) {
		subjectRepository.deleteById(id);

		flushCache();
	}

}