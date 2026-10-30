package com.okstatelibrary.oralhistoryworkflowcms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "predefineduser")
public class PredefinedUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private short Id;

	public short getId() {
		return this.Id;
	}

	public void setId(short value) {
		this.Id = value;
	}

	private byte UserType;

	public byte getUserType() {
		return this.UserType;
	}

	public void setUserType(byte value) {
		this.UserType = value;
	}

	private String Name;

	public String getName() {
		return this.Name;
	}

	public void setName(String value) {
		this.Name = value;
	}

	public PredefinedUser() {
		
	}
	
	public PredefinedUser(short id, byte type) {
		this.Id = id;
		this.UserType = type;
	}

	public PredefinedUser(short Id, byte UserType, String Name) {
		this.Id = Id;
		this.UserType = UserType;
		this.Name = Name;
	}
}