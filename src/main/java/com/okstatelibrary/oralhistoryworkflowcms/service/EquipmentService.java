package com.okstatelibrary.oralhistoryworkflowcms.service;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Equipments;

import java.util.List;
import java.util.Optional;

public interface EquipmentService {

	List<Equipments> findAll();
	
	Equipments saveEquipment(Equipments equipment);

	Optional<Equipments> findById(short Id);

	List<Equipments> findAllByType(byte type);

	void deleteEquipment(short id);
}