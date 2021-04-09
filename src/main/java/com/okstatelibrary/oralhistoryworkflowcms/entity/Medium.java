package com.okstatelibrary.oralhistoryworkflowcms.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mediums")
public class Medium {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private short Id;

	public short getId() {
		return this.Id;
	}

	public void setId(short value) {
		this.Id = value;
	}

	private String MediumName;

	public String getMediumName() {
		return this.MediumName;
	}

	public void setMediumName(String value) {
		this.MediumName = value;
	}

	public Medium() {
	}

	public Medium(short id) {
		this.Id = id;
	}
	
	public Medium(short id, String MediumName) {
		this.Id = id;
		this.MediumName = MediumName;
	}

}
