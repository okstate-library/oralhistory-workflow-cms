package com.okstatelibrary.oralhistoryworkflowcms.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

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
		this.EquipmentType = (byte)value;
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
