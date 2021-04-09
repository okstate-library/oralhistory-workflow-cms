package com.okstatelibrary.oralhistoryworkflowcms.service;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Medium;

import java.util.List;
import java.util.Optional;

public interface MediumService {

	List<Medium> findAll();
	
	Medium saveEquipment(Medium equipment);

	Optional<Medium> findById(short Id);

	void deleteMedium(short id);
}