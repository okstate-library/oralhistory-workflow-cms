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

            TranscriptDocument savedDoc = transcriptSearchRepository.save(document);

            return savedDoc;

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