package com.okstatelibrary.oralhistoryworkflowcms.entity;

import java.io.Serializable;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "repositories")
public class Repository implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1932156269043123514L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private short Id;

	public short getId() {
		return this.Id;
	}

	public void setId(short value) {
		this.Id = value;
	}

	private String RepositoryName;

	public String getRepositoryName() {
		return this.RepositoryName;
	}

	public void setRepositoryName(String value) {
		this.RepositoryName = value;
	}
	
	@OneToMany(mappedBy="repository")
    private Set<Collection> collections;
	
	public Repository() {
	}

	public Repository(short Id, String RepositoryName) {
		this.Id = Id;
		this.RepositoryName = RepositoryName;
	}
}