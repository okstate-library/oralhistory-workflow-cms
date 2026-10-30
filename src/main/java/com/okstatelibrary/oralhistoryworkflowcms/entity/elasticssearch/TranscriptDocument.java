package com.okstatelibrary.oralhistoryworkflowcms.entity.elasticssearch;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "transcript")
public class TranscriptDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String projectCode;

    public String getTranscriptLocation() {
        return TranscriptLocation;
    }

    public void setTranscriptLocation(String transcriptLocation) {
        TranscriptLocation = transcriptLocation;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getInterviewerDescription() {
        return InterviewerDescription;
    }

    public void setInterviewerDescription(String interviewerDescription) {
        InterviewerDescription = interviewerDescription;
    }

    public String getInterviewerKeywords() {
        return InterviewerKeywords;
    }

    public void setInterviewerKeywords(String interviewerKeywords) {
        InterviewerKeywords = interviewerKeywords;
    }

    public String getInterviewerSubjects() {
        return InterviewerSubjects;
    }

    public void setInterviewerSubjects(String interviewerSubjects) {
        InterviewerSubjects = interviewerSubjects;
    }

    public String getInterviewContext() {
        return InterviewContext;
    }

    public void setInterviewContext(String interviewContext) {
        InterviewContext = interviewContext;
    }

    public String getInterviewerNote() {
        return InterviewerNote;
    }

    public void setInterviewerNote(String interviewerNote) {
        InterviewerNote = interviewerNote;
    }

    public String getSensitiveContentDescription() {
        return SensitiveContentDescription;
    }

    public void setSensitiveContentDescription(String sensitiveContentDescription) {
        SensitiveContentDescription = sensitiveContentDescription;
    }

    @Field(type = FieldType.Text)
    private String TranscriptLocation;

    @Field(type = FieldType.Text)
    private String Title;

    @Field(type = FieldType.Text)
    private String InterviewerDescription;

    @Field(type = FieldType.Text)
    private String InterviewerKeywords;

    @Field(type = FieldType.Text)
    private String InterviewerSubjects;

    @Field(type = FieldType.Text)
    private String InterviewContext;

    @Field(type = FieldType.Text)
    private String InterviewerNote;

    @Field(type = FieldType.Text)
    private String SensitiveContentDescription;


    public TranscriptDocument() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

}