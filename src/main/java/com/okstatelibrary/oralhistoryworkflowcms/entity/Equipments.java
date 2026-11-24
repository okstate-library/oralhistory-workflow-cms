package com.okstatelibrary.oralhistoryworkflowcms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

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

	private short EquipmentType;

	public short getEquipmentType() {

		return this.EquipmentType;
	}

	public void setEquipmentType(short value) {
		this.EquipmentType = (short)value;
	}

	private String EquipmentUsedName;

	public String getEquipmentUsedName() {
		return this.EquipmentUsedName;
	}

	public void setEquipmentUsedName(String value) {
		this.EquipmentUsedName = value;
	}

	@Transient
	private String EquipmentTypeName;

	@Transient
	public String getEquipmentTypeName() {
		if (this.EquipmentType == com.okstatelibrary.oralhistoryworkflowcms.enums.EquipmentType.AUDIO.getCode()) {
			return "Audio";
		} else {
			return "Video";
		}
	}

	public Equipments() {
	}

	public Equipments(short id, byte EquipmentType, String EquipmentUsedName) {
		this.Id = id;
		this.EquipmentType = EquipmentType;
		this.EquipmentUsedName = EquipmentUsedName;
	}

	public Equipments(short id) {
		this.Id = id;
	}

}
