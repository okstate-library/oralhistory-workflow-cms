package com.okstatelibrary.oralhistoryworkflowcms.repository;

import com.okstatelibrary.oralhistoryworkflowcms.entity.elasticssearch.TranscriptDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TranscriptSearchRepository
        extends ElasticsearchRepository<TranscriptDocument, String> {
}