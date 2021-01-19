package com.okstatelibrary.oralhistoryworkflowcms.entity;

import java.util.List;

public class InterviewCreationDto {

	private List<Equipments> audioEquipments;
	
	private List<Collection> collections;

	public List<Collection> getCollections() {
		return collections;
	}

	public void setCollections(List<Collection> collections) {
		this.collections = collections;
	}

	public Transcription getTranscription() {
		return transcription;
	}

	public void setTranscription(Transcription transcription) {
		this.transcription = transcription;
	}

	public List<Equipments> getAudioEquipemnts() {
		return audioEquipments;
	}

	public void setAudioEquipemnts(List<Equipments> audioEquipments) {
		this.audioEquipments = audioEquipments;
	}

	private Transcription transcription;

}