package com.okstatelibrary.oralhistoryworkflowcms.service;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Equipments;

import java.util.List;

public interface EquipmentService {

	List<Equipments> findAll();
	
	List<Equipments> findAllAudios();
	
	List<Equipments> findAllVideos();
}