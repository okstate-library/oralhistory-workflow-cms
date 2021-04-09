package com.okstatelibrary.oralhistoryworkflowcms.entity.ui;

import java.util.List;

import com.okstatelibrary.oralhistoryworkflowcms.entity.*;

public class BrowsePageObject {

	private List<Transcription> transcriptions;

	public List<Transcription> getTranscriptions() {
		return transcriptions;
	}

	public void setTranscriptions(List<Transcription> transcriptions) {
		this.transcriptions = transcriptions;
	}

	/**
	 * @return the subseries
	 */
	public List<CheckBoxObject> getSubseries() {
		return subseries;
	}

	/**
	 * @param subseries the subseries to set
	 */
	public void setSubseries(List<CheckBoxObject> subseries) {
		this.subseries = subseries;
	}

	/**
	 * @return the interviewers
	 */
	public List<CheckBoxObject> getInterviewers() {
		return interviewers;
	}

	/**
	 * @param interviewers the interviewers to set
	 */
	public void setInterviewers(List<CheckBoxObject> interviewers) {
		this.interviewers = interviewers;
	}

	/**
	 * @return the collections
	 */
	public List<CheckBoxObject> getCollections() {
		return collections;
	}

	/**
	 * @param collections the collections to set
	 */
	public void setCollections(List<CheckBoxObject> collections) {
		this.collections = collections;
	}

	/**
	 * @return the statusOnline
	 */
	public List<CheckBoxObject> getStatusOnline() {
		return statusOnline;
	}

	/**
	 * @param statusOnline the statusOnline to set
	 */
	public void setStatusOnline(List<CheckBoxObject> statusOnline) {
		this.statusOnline = statusOnline;
	}

	/**
	 * @return the darkArchieve
	 */
	public List<CheckBoxObject> getDarkArchieve() {
		return darkArchieve;
	}

	/**
	 * @param darkArchieve the darkArchieve to set
	 */
	public void setDarkArchieve(List<CheckBoxObject> darkArchieve) {
		this.darkArchieve = darkArchieve;
	}

	/**
	 * @return the subjects
	 */
	public List<CheckBoxObject> getSubjects() {
		return subjects;
	}

	/**
	 * @param subjects the subjects to set
	 */
	public void setSubjects(List<CheckBoxObject> subjects) {
		this.subjects = subjects;
	}

	private List<CheckBoxObject> collections;

	private List<CheckBoxObject> interviewers;

	private List<CheckBoxObject> subseries;
	
	private List<CheckBoxObject> subjects;
	
	private List<CheckBoxObject> statusOnline;
	
	private List<CheckBoxObject> darkArchieve;

}
