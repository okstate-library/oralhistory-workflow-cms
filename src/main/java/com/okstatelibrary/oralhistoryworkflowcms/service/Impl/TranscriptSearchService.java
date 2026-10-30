package com.okstatelibrary.oralhistoryworkflowcms.service.Impl;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Transcription;
import com.okstatelibrary.oralhistoryworkflowcms.entity.elasticssearch.TranscriptDocument;
import com.okstatelibrary.oralhistoryworkflowcms.repository.TranscriptSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.Nonnull;

@Service
public class TranscriptSearchService {

    @Autowired
    private final TranscriptSearchRepository transcriptSearchRepository;

    public TranscriptSearchService(
            TranscriptSearchRepository transcriptSearchRepository) {
        this.transcriptSearchRepository = transcriptSearchRepository;
    }

    public TranscriptDocument saveTranscriptDocument(Transcription transcript) {

        try {

            TranscriptDocument document = getTranscriptDocument(transcript);

            System.out.println("========== BEFORE ELASTICSEARCH SAVE ==========");
            System.out.println("ID: " + document.getId());
            System.out.println("Project Code: " + document.getProjectCode());
            System.out.println("Title: " + document.getTitle());
            System.out.println("Transcript Location: " + document.getTranscriptLocation());
            System.out.println("Interviewer Description: " + document.getInterviewerDescription());
            System.out.println("Interviewer Keywords: " + document.getInterviewerKeywords());
            System.out.println("Interviewer Subjects: " + document.getInterviewerSubjects());
            System.out.println("Interview Context: " + document.getInterviewContext());
            System.out.println("Interviewer Note: " + document.getInterviewerNote());
            System.out.println("Sensitive Content Description: " + document.getSensitiveContentDescription());


            TranscriptDocument savedDoc = transcriptSearchRepository.save(document);

            System.out.println("========== AFTER ELASTICSEARCH SAVE ==========");
            System.out.println("Saved ID: " + savedDoc.getId());
            System.out.println("Saved Title: " + savedDoc.getTitle());
            System.out.println("Added " + savedDoc.getTitle());

            return savedDoc;

            //return null;


        } catch (Exception e) {
            System.out.println("Added " + e.fillInStackTrace());
        }

        return null;
    }

    @Nonnull
    private static TranscriptDocument getTranscriptDocument(Transcription transcript) {
        TranscriptDocument document = new TranscriptDocument();

        document.setId(String.valueOf(transcript.getId()));
        document.setProjectCode(transcript.getProjectCode());
        document.setInterviewContext(transcript.getInterviewContext());
        document.setInterviewerDescription(transcript.getInterviewerDescription());
        document.setTranscriptLocation(transcript.getTranscriptLocation());
        document.setInterviewerKeywords(transcript.getInterviewerKeywords());
        document.setTitle(transcript.getTitle());
        document.setInterviewerNote(transcript.getInterviewerNote());
        document.setInterviewerSubjects(transcript.getInterviewerSubjects());
        document.setSensitiveContentDescription(transcript.getSensitiveContentDescription());

        return document;
    }
}