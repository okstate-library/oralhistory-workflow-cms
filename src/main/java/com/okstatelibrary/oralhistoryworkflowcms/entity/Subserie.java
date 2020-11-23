package com.okstatelibrary.oralhistoryworkflowcms.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "subseries")
public class Subserie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int Id;

	public int getId() {
		return this.Id;
	}

	public void setId(int value) {
		this.Id = value;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CollectionId", insertable = false, updatable = false)
	private Collection collection;

	public Collection getCollection() {
		return this.collection;
	}

	public void setCollection(Collection value) {
		this.collection = value;
	}

	private short CollectionId;

	public short getCollectionId() {
		return this.CollectionId;
	}

	public void setCollectionId(short value) {
		this.CollectionId = value;
	}

	private String SubseriesName;

	public String getSubseriesName() {
		return this.SubseriesName;
	}

	public void setSubseriesName(String value) {
		this.SubseriesName = value;
	}

	private String Number;

	public String getNumber() {
		return this.Number;
	}

	public void setNumber(String value) {
		this.Number = value;
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

	private String Description;

	public String getDescription() {
		return this.Description;
	}

	public void setDescription(String value) {
		this.Description = value;
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

	private String Size;

	public String getSize() {
		return this.Size;
	}

	public void setSize(String value) {
		this.Size = value;
	}

	private String Acquisitioninfo;

	public String getAcquisitioninfo() {
		return this.Acquisitioninfo;
	}

	public void setAcquisitioninfo(String value) {
		this.Acquisitioninfo = value;
	}

	private String Language;

	public String getLanguage() {
		return this.Language;
	}

	public void setLanguage(String value) {
		this.Language = value;
	}

	private String PreservationNote;

	public String getPreservationNote() {
		return this.PreservationNote;
	}

	public void setPreservationNote(String value) {
		this.PreservationNote = value;
	}

	private String Rights;

	public String getRights() {
		return this.Rights;
	}

	public void setRights(String value) {
		this.Rights = value;
	}

	private String AccessRestrictions;

	public String getAccessRestrictions() {
		return this.AccessRestrictions;
	}

	public void setAccessRestrictions(String value) {
		this.AccessRestrictions = value;
	}

	private String PublicationRights;

	public String getPublicationRights() {
		return this.PublicationRights;
	}

	public void setPublicationRights(String value) {
		this.PublicationRights = value;
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

	private String SeparatedMaterial;

	public String getSeparatedMaterial() {
		return this.SeparatedMaterial;
	}

	public void setSeparatedMaterial(String value) {
		this.SeparatedMaterial = value;
	}

	private String OriginalLocation;

	public String getOriginalLocation() {
		return this.OriginalLocation;
	}

	public void setOriginalLocation(String value) {
		this.OriginalLocation = value;
	}

	private String CopiesLocation;

	public String getCopiesLocation() {
		return this.CopiesLocation;
	}

	public void setCopiesLocation(String value) {
		this.CopiesLocation = value;
	}

	private String PublicationNote;

	public String getPublicationNote() {
		return this.PublicationNote;
	}

	public void setPublicationNote(String value) {
		this.PublicationNote = value;
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

	private String ProcessedBy;

	public String getProcessedBy() {
		return this.ProcessedBy;
	}

	public void setProcessedBy(String value) {
		this.ProcessedBy = value;
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

	public Subserie() {

	}

	public Subserie(int Id) {
		this.Id = Id;
	}

	public Subserie(int Id, short CollectionId, String SubseriesName, String Number, String Dates, String Note,
			String Subjects, String Keywords, String Description, String ScopeAndContent, String CustodialHistory,
			String Size, String Acquisitioninfo, String Language, String PreservationNote, String Rights,
			String AccessRestrictions, String PublicationRights, String PreferredCitation, String RelatedCollection,
			String SeparatedMaterial, String OriginalLocation, String CopiesLocation, String PublicationNote,
			String Creator, String Contributors, String ProcessedBy, String Sponsors, int CreatedBy,
			java.sql.Date CreatedDate, int UpdatedBy, java.sql.Date UpdatedDate) {
		this.Id = Id;
		this.CollectionId = CollectionId;
		this.SubseriesName = SubseriesName;
		this.Number = Number;
		this.Dates = Dates;
		this.Note = Note;
		this.Subjects = Subjects;
		this.Keywords = Keywords;
		this.Description = Description;
		this.ScopeAndContent = ScopeAndContent;
		this.CustodialHistory = CustodialHistory;
		this.Size = Size;
		this.Acquisitioninfo = Acquisitioninfo;
		this.Language = Language;
		this.PreservationNote = PreservationNote;
		this.Rights = Rights;
		this.AccessRestrictions = AccessRestrictions;
		this.PublicationRights = PublicationRights;
		this.PreferredCitation = PreferredCitation;
		this.RelatedCollection = RelatedCollection;
		this.SeparatedMaterial = SeparatedMaterial;
		this.OriginalLocation = OriginalLocation;
		this.CopiesLocation = CopiesLocation;
		this.PublicationNote = PublicationNote;
		this.Creator = Creator;
		this.Contributors = Contributors;
		this.ProcessedBy = ProcessedBy;
		this.Sponsors = Sponsors;
		this.CreatedBy = CreatedBy;
		this.CreatedDate = CreatedDate;
		this.UpdatedBy = UpdatedBy;
		this.UpdatedDate = UpdatedDate;
	}
}