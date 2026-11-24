package com.okstatelibrary.oralhistoryworkflowcms.service.Impl;

import com.okstatelibrary.oralhistoryworkflowcms.entity.Transcription;
import com.okstatelibrary.oralhistoryworkflowcms.repository.TranscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElasticsearchSyncService {

    private static final Logger logger =
            LoggerFactory.getLogger(ElasticsearchSyncService.class);

    private final TranscriptionRepository transcriptionRepository;
    private final TranscriptSearchService transcriptSearchService;

//    public ElasticsearchSyncService(
//    ) {
//        this.transcriptionRepository = new TranscriptionRepository();
//        this.transcriptSearchService = new TranscriptSearchService();
//    }

    @Autowired
    public ElasticsearchSyncService(
            TranscriptionRepository transcriptionRepository,
            TranscriptSearchService transcriptSearchService) {

        this.transcriptionRepository = transcriptionRepository;
        this.transcriptSearchService = transcriptSearchService;
    }

    //@Scheduled(fixedDelay = 60000)
    public synchronized void synchronizeData() {

        logger.info("Starting transcription synchronization with Elasticsearch.");

        try {
            List<Transcription> transcriptions =
                    transcriptionRepository.findAll();

            int successCount = 0;
            int failureCount = 0;

            for (Transcription transcription : transcriptions) {

                try {
                    transcriptSearchService.saveTranscriptDocument(transcription);
                    successCount++;

                } catch (Exception e) {
                    failureCount++;

                    logger.error(
                            "Failed to synchronize transcription with ID: {}",
                            transcription.getId(),
                            e
                    );
                }
            }

            logger.info(
                    "Elasticsearch synchronization completed. Total: {}, Successful: {}, Failed: {}",
                    transcriptions.size(),
                    successCount,
                    failureCount
            );

        } catch (Exception e) {
            logger.error(
                    "Unable to retrieve transcriptions for Elasticsearch synchronization.",
                    e
            );
        }
    }
}