package com.okstatelibrary.oralhistoryworkflowcms.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "subjects")
public class Subject {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private short Id;

	public short getId() {
		return this.Id;
	}

	public void setId(short value) {
		this.Id = value;
	}

	private String SubjectName;

	public String getSubjectName() {
		return this.SubjectName;
	}

	public void setSubjectName(String value) {
		this.SubjectName = value;
	}

	public Subject() {
	}

	public Subject(short Id, String SubjectName) {
		this.Id = Id;
		this.SubjectName = SubjectName;
	}
}