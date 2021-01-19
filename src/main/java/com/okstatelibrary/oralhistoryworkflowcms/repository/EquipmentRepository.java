package com.okstatelibrary.oralhistoryworkflowcms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Equipments;

public interface EquipmentRepository extends JpaRepository<Equipments, Short> {

	List<Equipments> findAll();

//	List<Equipments> findAllAudios();
//
//	List<Equipments> findAllVideos();
}