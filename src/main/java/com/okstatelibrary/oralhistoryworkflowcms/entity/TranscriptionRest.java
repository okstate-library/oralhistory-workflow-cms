package com.okstatelibrary.oralhistoryworkflowcms.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

public class TranscriptionRest { // implements Serializable

	/**
	 * 
	 */
	// private static final long serialVersionUID = -2750503608069646012L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private int Id;

	public int getId() {
		return this.Id;
	}

	public void setId(int value) {
		this.Id = value;
	}

	private String ProjectCode;

	public String getProjectCode() {
		return this.ProjectCode;
	}

	public void setProjectCode(String value) {
		this.ProjectCode = value;
	}

//	private short CollectionId;
//
//	public short getCollectionId() {
//		return this.CollectionId;
//	}
//
//	public void setCollectionId(short value) {
//		this.CollectionId = value;
//	}
//
//	@ManyToOne
//	@JoinColumn(name = "CollectionId", insertable = false, updatable = false)
//	private Collection tcollection;
//
//	@JsonBackReference
//	public Collection getCollection() {
//		return this.tcollection;
//	}
//
//	public void setCollection(Collection value) {
//		this.tcollection = value;
//	}
//
//	@Transient
	private String CollectionName;

	@Transient
	public String getCollectionName() {

//		if (this.tcollection != null) {
//			CollectionName = this.tcollection.getCollectionName();
//		}
		return CollectionName;
	}

	@Transient
	public void setCollectionName(String value) {
		this.CollectionName = value;
	}

//	private int SeriesId;
//
//	public int getSeriesId() {
//		return this.SeriesId;
//	}
//
//	public void setSeriesId(int value) {
//		this.SeriesId = value;
//	}
//
//	@ManyToOne
//	@JoinColumn(name = "SeriesId", insertable = false, updatable = false)
//	private Series series;
//
//	@JsonBackReference
//	public Series getseries() {
//		return this.series;
//	}
//
//	public void setSeries(Series value) {
//		this.series = value;
//	}
//
//	@Transient
	private String SeriesName;

	@Transient
	public String getSeriesName() {

//		if (this.series != null) {
//			SeriesName = this.series.getSeriesName();
//		}
		return SeriesName;
	}

	@Transient
	public void setSeriesName(String value) {
		this.SeriesName = value;
	}

	private String Interviewee;

	public String getInterviewee() {
		return this.Interviewee;
	}

	public void setInterviewee(String value) {
		this.Interviewee = value;
	}

	private String Interviewer;

	public String getInterviewer() {
		return this.Interviewer;
	}

	public void setInterviewer(String value) {
		this.Interviewer = value;
	}

	private String InterviewDates;

	public String getInterviewDates() {
		return InterviewDates;
	}

	public void setInterviewDates(String value) {
		this.InterviewDates = value;
	}

	private String Description;

	public String getDescription() {
		return this.Description;
	}

	public void setDescription(String value) {
		this.Description = value;
	}

	private String Keywords;

	public String getKeywords() {
		return this.Keywords;
	}

	public void setKeywords(String value) {
		this.Keywords = value;
	}

	private String Subject;

	public String getSubject() {
		return this.Subject;
	}

	public void setSubject(String value) {
		this.Subject = value;
	}

	private String ScopeAndContents;

	public String getScopeAndContents() {
		return this.ScopeAndContents;
	}

	public void setScopeAndContents(String value) {
		this.ScopeAndContents = value;
	}

	private String Format;

	public String getFormat() {
		return this.Format;
	}

	public void setFormat(String value) {
		this.Format = value;
	}

	private String CoverageSpatial;

	public String getCoverageSpatial() {
		return this.CoverageSpatial;
	}

	public void setCoverageSpatial(String value) {
		this.CoverageSpatial = value;
	}

	private String CoverageTemporal;

	public String getCoverageTemporal() {
		return this.CoverageTemporal;
	}

	public void setCoverageTemporal(String value) {
		this.CoverageTemporal = value;
	}

	private String CONTENTdmURL;

	public String getCONTENTdmURL() {
		return this.CONTENTdmURL;
	}

	public void setCONTENTdmURL(String value) {
		this.CONTENTdmURL = value;
	}

	private byte IsOnline;

	public byte getIsOnline() {
		return this.IsOnline;
	}

	public void setIsOnline(byte value) {
		this.IsOnline = value;
	}

	private String Rights;

	/**
	 * @return the rights
	 */
	public String getRights() {
		return Rights;
	}

	/**
	 * @param rights the rights to set
	 */
	public void setRights(String rights) {
		Rights = rights;
	}

	public TranscriptionRest() {

	}

	private String Type;

	public String getType() {
		return this.Type;
	}

	public void setType(String value) {
		this.Type = value;
	}
	
	private String Publisher;

	public String getPublisher() {
		return this.Publisher;
	}

	public void setPublisher(String value) {
		this.Publisher = value;
	}
	
	private byte IsRestriction;

	public byte getIsRestriction() {
		return this.IsRestriction;
	}

	public void setIsRestriction(byte value) {
		this.IsRestriction = value;
	}
}
