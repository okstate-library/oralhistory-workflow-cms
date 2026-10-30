package com.okstatelibrary.oralhistoryworkflowcms.entity;

import java.text.ParseException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Transactional
@Table(name = "transcriptions")
public class Transcription { // implements Serializable

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

	//@NotNull(message = "Please enter a project code")
	private String ProjectCode;

	public String getProjectCode() {
		return this.ProjectCode;
	}

	public void setProjectCode(String value) {
		this.ProjectCode = value;
	}

	private byte IsPriority;

	public byte getIsPriority() {
		return this.IsPriority;
	}

	public void setIsPriority(byte value) {
		this.IsPriority = value;
	}

	private String ReasonForPriority;

	public String getReasonForPriority() {
		return this.ReasonForPriority;
	}

	public void setReasonForPriority(String value) {
		this.ReasonForPriority = value;
	}

	private String InitialNote;

	public String getInitialNote() {
		return this.InitialNote;
	}

	public void setInitialNote(String value) {
		this.InitialNote = value;
	}

	private short CollectionId;

	public short getCollectionId() {
		return this.CollectionId;
	}

	public void setCollectionId(short value) {
		this.CollectionId = value;
	}

	@ManyToOne
	@JoinColumn(name = "CollectionId", insertable = false, updatable = false)
	private Collection tcollection;

	@JsonBackReference
	public Collection getCollection() {
		return this.tcollection;
	}

	public void setCollection(Collection value) {
		this.tcollection = value;
	}

	@Transient
	private String CollectionName;

	@Transient
	public String getCollectionName() {

		if (this.tcollection != null) {
			CollectionName = this.tcollection.getCollectionName();
		}
		return CollectionName;
	}

	@Transient
	public void setCollectionName(String value) {
		this.CollectionName = value;
	}

	private int SeriesId;

	public int getSeriesId() {
		return this.SeriesId;
	}

	public void setSeriesId(int value) {
		this.SeriesId = value;
	}

	@ManyToOne
	@JoinColumn(name = "SeriesId", insertable = false, updatable = false)
	private Series series;

	@JsonBackReference
	public Series getseries() {
		return this.series;
	}

	public void setSeries(Series value) {
		this.series = value;
	}

	@Transient
	private String SeriesName;

	@Transient
	public String getSeriesName() {

		if (this.series != null) {
			SeriesName = this.series.getSeriesName();
		}
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

	private String Title;

	public String getTitle() {
		return this.Title;
	}

	public void setTitle(String value) {
		this.Title = value;
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

	private String InterviewerDescription;

	public String getInterviewerDescription() {
		return this.InterviewerDescription;
	}

	public void setInterviewerDescription(String value) {
		this.InterviewerDescription = value;
	}

	private String InterviewerKeywords;

	public String getInterviewerKeywords() {
		return this.InterviewerKeywords;
	}

	public void setInterviewerKeywords(String value) {
		this.InterviewerKeywords = value;
	}

	private String InterviewerSubjects;

	public String getInterviewerSubjects() {
		return this.InterviewerSubjects;
	}

	public void setInterviewerSubjects(String value) {
		this.InterviewerSubjects = value;
	}

	private String InterviewContext;

	public String getInterviewContext() {
		return this.InterviewContext;
	}

	public void setInterviewContext(String value) {
		this.InterviewContext = value;
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

	private String RelationIsPartOf;

	public String getRelationIsPartOf() {
		return this.RelationIsPartOf;
	}

	public void setRelationIsPartOf(String value) {
		this.RelationIsPartOf = value;
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

	private String OHMSURL;

	public String getOHMSURL() {
		return this.OHMSURL;
	}

	public void setOHMSURL(String value) {
		this.OHMSURL = value;
	}

	private String YouTubeURL;

	public String getYouTubeURL() {
		return this.YouTubeURL;
	}

	public void setYouTubeURL(String value) {
		this.YouTubeURL = value;
	}

	private String Rights;

	public String getRights() {
		return this.Rights;
	}

	public void setRights(String value) {
		this.Rights = value;
	}

	private String Language;

	public String getLanguage() {
		return this.Language;
	}

	public void setLanguage(String value) {
		this.Language = value;
	}

	private String Transcript;

	public String getTranscript() {
		return this.Transcript;
	}

	public void setTranscript(String value) {
		this.Transcript = value;
	}

	private String FileName;

	public String getFileName() {
		return this.FileName;
	}

	public void setFileName(String value) {
		this.FileName = value;
	}

	private byte IsOnline;

	public byte getIsOnline() {
		return this.IsOnline;
	}

	public void setIsOnline(byte value) {
		this.IsOnline = value;
	}

	private String OnlineDate;

	public String getOnlineDate() {
		return this.OnlineDate;
	}

	public void setOnlineDate(String value) {
		this.OnlineDate = value;
	}

	private byte IsRosetta;

	public byte getIsRosetta() {
		return this.IsRosetta;
	}

	public void setIsRosetta(byte value) {
		this.IsRosetta = value;
	}

//	private byte IsRosettaForm;
//
//	public byte getIsRosettaForm() {
//		return this.IsRosettaForm;
//	}
//
//	public void setIsRosettaForm(byte value) {
//		this.IsRosettaForm = value;
//	}

	private byte IsRestriction;

	public byte getIsRestriction() {
		return this.IsRestriction;
	}

	public void setIsRestriction(byte value) {
		this.IsRestriction = value;
	}

	private String RestrictionNote;

	public String getRestrictionNote() {
		return this.RestrictionNote;
	}

	public void setRestrictionNote(String value) {
		this.RestrictionNote = value;
	}

	private byte IsDarkArchive;

	public byte getIsDarkArchive() {
		return this.IsDarkArchive;
	}

	public void setIsDarkArchive(byte value) {
		this.IsDarkArchive = value;
	}

	private String DarkArchiveNote;

	public String getDarkArchiveNote() {
		return this.DarkArchiveNote;
	}

	public void setDarkArchiveNote(String value) {
		this.DarkArchiveNote = value;
	}

	private String AudioEquipmentUsed;

	public String getAudioEquipmentUsed() {
		return this.AudioEquipmentUsed;
	}

	public void setAudioEquipmentUsed(String value) {
		this.AudioEquipmentUsed = value;
	}

	private String VideoEquipmentUsed;

	public String getVideoEquipmentUsed() {
		return this.VideoEquipmentUsed;
	}

	public void setVideoEquipmentUsed(String value) {
		this.VideoEquipmentUsed = value;
	}

	private String DigitalCameraUsed;

	public String getDigitalCameraUsed() {
		return this.DigitalCameraUsed;
	}

	public void setDigitalCameraUsed(String value) {
		this.DigitalCameraUsed = value;
	}

	private String EquipmentNumber;

	public String getEquipmentNumber() {
		return this.EquipmentNumber;
	}

	public void setEquipmentNumber(String value) {
		this.EquipmentNumber = value;
	}

	private String InterviewerNote;

	public String getInterviewerNote() {
		return this.InterviewerNote;
	}

	public void setInterviewerNote(String value) {
		this.InterviewerNote = value;
	}

	private String AccessionDate;

	private String AccessionNote;

	private String GeneralNote;

	public String getGeneralNote() {
		return this.GeneralNote;
	}

	public void setGeneralNote(String value) {
		this.GeneralNote = value;
	}

	private String NarratorBio;

	/**
	 * @return the narratorBio
	 */
	public String getNarratorBio() {
		return NarratorBio;
	}

	/**
	 * @param narratorBio the narratorBio to set
	 */
	public void setNarratorBio(String narratorBio) {
		NarratorBio = narratorBio;
	}

	private String UsageInformation;

	/**
	 * @return the narratorBio
	 */
	public String getUsageInformation() {
		return UsageInformation;
	}

	/**
	 * @param narratorBio the narratorBio to set
	 */
	public void setUsageInformation(String usageInformation) {
		UsageInformation = usageInformation;
	}

	private byte ReleaseForm;

	public byte getReleaseForm() {
		return this.ReleaseForm;
	}

	public void setReleaseForm(byte value) {
		this.ReleaseForm = value;
	}

	private String TranscriberAssigned;

	public String getTranscriberAssigned() {
		return this.TranscriberAssigned;
	}

	public void setTranscriberAssigned(String value) {
		this.TranscriberAssigned = value;
	}

	private String AuditCheckCompleted;

	public String getAuditCheckCompleted() {
		return this.AuditCheckCompleted;
	}

	public void setAuditCheckCompleted(String value) {
		this.AuditCheckCompleted = value;
	}

	private String FirstEditCompleted;

	public String getFirstEditCompleted() {
		return this.FirstEditCompleted;
	}

	public void setFirstEditCompleted(String value) {
		this.FirstEditCompleted = value;
	}

	private String SecondEditCompleted;

	public String getSecondEditCompleted() {
		return this.SecondEditCompleted;
	}

	public void setSecondEditCompleted(String value) {
		this.SecondEditCompleted = value;
	}

	private String ThirdEditCompleted;

	public String getThirdEditCompleted() {
		return this.ThirdEditCompleted;
	}

	public void setThirdEditCompleted(String value) {
		this.ThirdEditCompleted = value;
	}

	private String DraftSentDate;

	public String getDraftSentDate() {
		return this.DraftSentDate;
	}

	public void setDraftSentDate(String value) {
		this.DraftSentDate = value;
	}

	private String EditWithCorrectionCompleted;

	public String getEditWithCorrectionCompleted() {
		return this.EditWithCorrectionCompleted;
	}

	public void setEditWithCorrectionCompleted(String value) {
		this.EditWithCorrectionCompleted = value;
	}

	private String FinalEditCompleted;

	public String getFinalEditCompleted() {
		return this.FinalEditCompleted;
	}

	public void setFinalEditCompleted(String value) {
		this.FinalEditCompleted = value;
	}

	private String FinalSentDate;

	public String getFinalSentDate() {
		return this.FinalSentDate;
	}

	public void setFinalSentDate(String value) {
		this.FinalSentDate = value;
	}

//	private byte TranscriptStatus;
//
//	public byte getTranscriptStatus() {
//		return this.TranscriptStatus;
//	}
//
//	public void setTranscriptStatus(byte value) {
//		this.TranscriptStatus = value;
//	}

	private String MetadataDraft;

	public String getMetadataDraft() {
		return this.MetadataDraft;
	}

	public void setMetadataDraft(String value) {
		this.MetadataDraft = value;
	}

	private String TranscriptNote;

	public String getTranscriptNote() {
		return this.TranscriptNote;
	}

	public void setTranscriptNote(String value) {
		this.TranscriptNote = value;
	}

	private byte IsAudioFormat;

	public byte getIsAudioFormat() {
		return this.IsAudioFormat;
	}

	public void setIsAudioFormat(byte value) {
		this.IsAudioFormat = value;
	}

	private byte IsVideoFormat;

	public byte getIsVideoFormat() {
		return this.IsVideoFormat;
	}

	public void setIsVideoFormat(byte value) {
		this.IsVideoFormat = value;
	}

	private String OriginalAudioDuration;

	public String getOriginalAudioDuration() {
		return this.OriginalAudioDuration;
	}

	public void setOriginalAudioDuration(String value) {
		this.OriginalAudioDuration = value;
	}

	private String OriginalVideoDuration;

	public String getOriginalVideoDuration() {
		return this.OriginalVideoDuration;
	}

	public void setOriginalVideoDuration(String value) {
		this.OriginalVideoDuration = value;
	}

	private String EditedAudioDuration;

	public String getEditedAudioDuration() {
		return this.EditedAudioDuration;
	}

	public void setEditedAudioDuration(String value) {
		this.EditedAudioDuration = value;
	}

	private String EditedVideoDuration;

	public String getEditedVideoDuration() {
		return this.EditedVideoDuration;
	}

	public void setEditedVideoDuration(String value) {
		this.EditedVideoDuration = value;
	}

	private byte IsBornDigital;

	public byte getIsBornDigital() {
		return this.IsBornDigital;
	}

	public void setIsBornDigital(byte value) {
		this.IsBornDigital = value;
	}

	private String OriginalMedium;

	public String getOriginalMedium() {
		return this.OriginalMedium;
	}

	public void setOriginalMedium(String value) {
		this.OriginalMedium = value;
	}

	private byte IsConvertToDigital;

	public byte getIsConvertToDigital() {
		return this.IsConvertToDigital;
	}

	public void setIsConvertToDigital(byte value) {
		this.IsConvertToDigital = value;
	}

//	private String ConvertDigitalDate;
//	
//	public String getConvertDigitalDate() {
//		return this.ConvertDigitalDate;
//	}
//
//	public void setConvertDigitalDate(String value) {
//		this.ConvertDigitalDate = value;
//	}

	private String ConvertToDigitalDate;

	public String getConvertToDigitalDate() throws ParseException {

		return this.ConvertToDigitalDate;

	}

	public void setConvertToDigitalDate(String value) {
		this.ConvertToDigitalDate = value;
	}

	private String MediaNote;

	public String getMediaNote() {
		return this.MediaNote;
	}

	public void setMediaNote(String value) {
		this.MediaNote = value;
	}

	private String DigitizationNote;

	public String getDigitizationNote() {
		return this.DigitizationNote;
	}

	public void setDigitizationNote(String value) {
		this.DigitizationNote = value;
	}

	private String TranscriptSize;

	public String getTranscriptSize() {
		return this.TranscriptSize;
	}

	public void setTranscriptSize(String value) {
		this.TranscriptSize = value;
	}

	private String NumberOfImages;

	public String getNumberOfImages() {
		return this.NumberOfImages;
	}

	public void setNumberOfImages(String value) {
		this.NumberOfImages = value;
	}

	private String ImagesNote;

	public String getImagesNote() {
		return this.ImagesNote;
	}

	public void setImagesNote(String value) {
		this.ImagesNote = value;
	}

	private String NumberOfDocuments;

	public String getNumberOfDocuments() {
		return this.NumberOfDocuments;
	}

	public void setNumberOfDocuments(String value) {
		this.NumberOfDocuments = value;
	}

	private String DocumentsNote;

	public String getDocumentsNote() {
		return this.DocumentsNote;
	}

	public void setDocumentsNote(String value) {
		this.DocumentsNote = value;
	}

	private byte IsProductionMasterFileStatus;

	public byte getIsProductionMasterFileStatus() {
		return this.IsProductionMasterFileStatus;
	}

	public void setIsProductionMasterFileStatus(byte value) {
		this.IsProductionMasterFileStatus = value;
	}

	private byte IsNarratorRevisionReceived;

	public byte getIsNarratorRevisionReceived() {
		return this.IsNarratorRevisionReceived;
	}

	public void setIsNarratorRevisionReceived(byte value) {
		this.IsNarratorRevisionReceived = value;
	}

	private String TechnicalSpecification;

	public String getTechnicalSpecification() {
		return this.TechnicalSpecification;
	}

	public void setTechnicalSpecification(String value) {
		this.TechnicalSpecification = value;
	}

	private String TranscriptLocation;

	public String getTranscriptLocation() {
		return this.TranscriptLocation;
	}

	public void setTranscriptLocation(String value) {
		this.TranscriptLocation = value;
	}

	private byte SentOut;

	public byte getSentOut() {
		return this.SentOut;
	}

	public void setSentOut(byte value) {
		this.SentOut = value;
	}

	private String RelatedInterviews;

	public String getRelatedInterviews() {
		return RelatedInterviews;
	}

	public void setRelatedInterviews(String value) {
		this.RelatedInterviews = value;
	}

	private byte IsPublic;

	public byte getIsPublic() {
		return this.IsPublic;
	}

	public void setIsPublic(byte value) {
		this.IsPublic = value;
	}

	private byte IsPotentiallySensitiveContent;

	/**
	 * @return the isPotentiallySensitiveContent
	 */
	public byte getIsPotentiallySensitiveContent() {
		return IsPotentiallySensitiveContent;
	}

	/**
	 * @param isPotentiallySensitiveContent the isPotentiallySensitiveContent to set
	 */
	public void setIsPotentiallySensitiveContent(byte isPotentiallySensitiveContent) {
		IsPotentiallySensitiveContent = isPotentiallySensitiveContent;
	}

	private String SensitiveContentDescription;

	/**
	 * @return the sensitiveContentDescription
	 */
	public String getSensitiveContentDescription() {
		return SensitiveContentDescription;
	}

	/**
	 * @param sensitiveContentDescription the sensitiveContentDescription to set
	 */
	public void setSensitiveContentDescription(String sensitiveContentDescription) {
		SensitiveContentDescription = sensitiveContentDescription;
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

	public Transcription() {

	}

	@Override
	public String toString() {
		return "Person [id=" + Id + "CollectionId " + CollectionId + " collection name=" + CollectionName + "]";
	}

    public String getAccessionNote() {
        return AccessionNote;
    }

    public void setAccessionNote(String accessionNote) {
        AccessionNote = accessionNote;
    }

    public String getAccessionDate() {
        return AccessionDate;
    }

    public void setAccessionDate(String accessionDate) {
        AccessionDate = accessionDate;
    }
}
