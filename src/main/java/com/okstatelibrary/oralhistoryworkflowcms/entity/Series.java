package com.okstatelibrary.oralhistoryworkflowcms.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "series")
public class Series {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int Id;

	public int getId() {
		return this.Id;
	}

	public void setId(int value) {
		this.Id = value;
	}

	@OneToMany(mappedBy = "series")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Transcription> transcriptions;

	@JsonManagedReference
	public List<Transcription> getTranscriptions() {
		return this.transcriptions;
	}

	public void setTranscriptions(List<Transcription> value) {
		this.transcriptions = value;
	}

	private int CollectionId;
	
	public int getCollectionId() {
		return CollectionId;
	}

	public void setCollectionId(int collectionId) {
		CollectionId = collectionId;
	}
	
	@ManyToOne
	@JoinColumn(name = "CollectionId", insertable = false, updatable = false)
	private Collection collection;

	@JsonBackReference
	public Collection getCollection() {
		return this.collection;
	}

	public void setCollection(Collection value) {
		this.collection = value;
	}

	private String SeriesName;

	public String getSeriesName() {
		return this.SeriesName;
	}

	public void setSeriesName(String value) {
		this.SeriesName = value;
	}

	private String Code;

	public String getCode() {
		return this.Code;
	}

	public void setCode(String value) {
		this.Code = value;
	}

	private String Dates;

	public String getDates() {
		return this.Dates;
	}

	public void setDates(String value) {
		this.Dates = value;
	}

	private String Note;

	public String getNote() {
		return this.Note;
	}

	public void setNote(String value) {
		this.Note = value;
	}

	private String Description;

	public String getDescription() {
		return this.Description;
	}

	public void setDescription(String value) {
		this.Description = value;
	}

	private String Subjects;

	public String getSubjects() {
		return this.Subjects;
	}

	public void setSubjects(String value) {
		this.Subjects = value;
	}

	private String Keywords;

	public String getKeywords() {
		return this.Keywords;
	}

	public void setKeywords(String value) {
		this.Keywords = value;
	}

	private String LandingPageUrl;

	public String getLandingPageUrl() {
		return this.LandingPageUrl;
	}

	public void setLandingPageUrl(String value) {
		this.LandingPageUrl = value;
	}

	private String ScopeAndContent;

	public String getScopeAndContent() {
		return this.ScopeAndContent;
	}

	public void setScopeAndContent(String value) {
		this.ScopeAndContent = value;
	}

	private String CustodialHistory;

	public String getCustodialHistory() {
		return this.CustodialHistory;
	}

	public void setCustodialHistory(String value) {
		this.CustodialHistory = value;
	}

	private String Acquisitioninfo;

	public String getAcquisitioninfo() {
		return this.Acquisitioninfo;
	}

	public void setAcquisitioninfo(String value) {
		this.Acquisitioninfo = value;
	}

	private short NumberOfInterviews;

	public short getNumberOfInterviews() {
		return this.NumberOfInterviews;
	}

	public void setNumberOfInterviews(short value) {
		
		this.NumberOfInterviews = value;
	}

	private String Language;

	public String getLanguage() {
		return this.Language;
	}

	public void setLanguage(String value) {
		this.Language = value;
	}

	private String Publisher;

	public String getPublisher() {
		return this.Publisher;
	}

	public void setPublisher(String value) {
		this.Publisher = value;
	}

	private String Rights;

	public String getRights() {
		return this.Rights;
	}

	public void setRights(String value) {
		this.Rights = value;
	}

	private String PreservationNote;

	public String getPreservationNote() {
		return this.PreservationNote;
	}

	public void setPreservationNote(String value) {
		this.PreservationNote = value;
	}

	private String Format;

	public String getFormat() {
		return this.Format;
	}

	public void setFormat(String value) {
		this.Format = value;
	}

	private String Type;

	public String getType() {
		return this.Type;
	}

	public void setType(String value) {
		this.Type = value;
	}

	private String AccessRestrictions;

	public String getAccessRestrictions() {
		return this.AccessRestrictions;
	}

	public void setAccessRestrictions(String value) {
		this.AccessRestrictions = value;
	}

	private String PreferredCitation;

	public String getPreferredCitation() {
		return this.PreferredCitation;
	}

	public void setPreferredCitation(String value) {
		this.PreferredCitation = value;
	}

	private String RelatedCollection;

	public String getRelatedCollection() {
		return this.RelatedCollection;
	}

	public void setRelatedCollection(String value) {
		this.RelatedCollection = value;
	}

	private String ProjectProcess;

	public String getProjectProcess() {
		return this.ProjectProcess;
	}

	public void setProjectProcess(String value) {
		this.ProjectProcess = value;
	}

	private String Creator;

	public String getCreator() {
		return this.Creator;
	}

	public void setCreator(String value) {
		this.Creator = value;
	}

	private String Contributors;

	public String getContributors() {
		return this.Contributors;
	}

	public void setContributors(String value) {
		this.Contributors = value;
	}

	private String Sponsors;

	public String getSponsors() {
		return this.Sponsors;
	}

	public void setSponsors(String value) {
		this.Sponsors = value;
	}

	private int CreatedBy;

	public int getCreatedBy() {
		return this.CreatedBy;
	}

	public void setCreatedBy(int value) {
		this.CreatedBy = value;
	}

	private java.sql.Date CreatedDate;

	public java.sql.Date getCreatedDate() {
		return this.CreatedDate;
	}

	public void setCreatedDate(java.sql.Date value) {
		this.CreatedDate = value;
	}

	private int UpdatedBy;

	public int getUpdatedBy() {
		return this.UpdatedBy;
	}

	public void setUpdatedBy(int value) {
		this.UpdatedBy = value;
	}

	private java.sql.Date UpdatedDate;

	public java.sql.Date getUpdatedDate() {
		return this.UpdatedDate;
	}

	public void setUpdatedDate(java.sql.Date value) {
		this.UpdatedDate = value;
	}

	public Series() {

	}

	public Series(int Id) {
		this.Id = Id;
	}
}