package com.okstatelibrary.oralhistoryworkflowcms.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "equipments")
public class Equipments {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private short Id;

	public short getId() {
		return this.Id;
	}

	public void setId(short value) {
		this.Id = value;
	}

	private byte EquipmentType;

	public byte getEquipmentType() {
		return this.EquipmentType;
	}

	public void setEquipmentType(byte value) {
		this.EquipmentType = value;
	}

	private String EquipmentUsedName;

	public String getEquipmentUsedName() {
		return this.EquipmentUsedName;
	}

	public void setEquipmentUsedName(String value) {
		this.EquipmentUsedName = value;
	}

	
	public Equipments() {
	}

	public Equipments(short id, byte EquipmentType, String EquipmentUsedName) {
		this.Id = id;
		this.EquipmentType = EquipmentType;
		this.EquipmentUsedName = EquipmentUsedName;
	}
}
