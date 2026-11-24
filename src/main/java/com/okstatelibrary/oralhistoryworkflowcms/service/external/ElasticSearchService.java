package com.okstatelibrary.oralhistoryworkflowcms.service.external;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okstatelibrary.oralhistoryworkflowcms.entity.elasticssearch.Hit;
import com.okstatelibrary.oralhistoryworkflowcms.entity.elasticssearch.Root;
import com.okstatelibrary.oralhistoryworkflowcms.entity.elasticssearch.Source;
import com.okstatelibrary.oralhistoryworkflowcms.util.AppSystemProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ElasticSearchService {

    private final AppSystemProperties applicationConfig;

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchService.class);

    private RestTemplate restTemplate = new RestTemplate();

    ObjectMapper mapper = new ObjectMapper();

    public ElasticSearchService(AppSystemProperties applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    private HttpHeaders getHttpHeaders() {

        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/json");
        headers.setBasicAuth("elastic", "abc123");

        return headers;

    }

    ObjectMapper objectMapper = new ObjectMapper();


    public String getSearchList(String searchWord)
            throws RestClientException {

        try {

            String url =  this.applicationConfig.getElasticsearchUrl() + "/transcript/_search";

            System.out.println("url - " + url );

            //String url = "http://localhost:9200/transcript/_search";

            Map<String, Object> jsonBody = Map.of(
                    "size", 1000,
                    "_source", List.of("id"),
                    "query", Map.of(
                            "multi_match", Map.of(
                                    "query", searchWord,
                                    "fields", List.of("*")
                            )
                    )
            );

            String jsonString = objectMapper.writeValueAsString(jsonBody);

            HttpEntity<String> request = new HttpEntity<>(jsonString, getHttpHeaders());

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(response.getBody().toString());

            String ids = StreamSupport.stream(
                            root.path("hits").path("hits").spliterator(), false)
                    .map(hit -> hit.path("_source").path("id").asText())
                    .filter(id -> !id.isBlank())
                    .collect(Collectors.joining(","));

            return ids;

        } catch (Exception e) {

            System.err.println("Error: FolioService : getItembyBarcode " + e.getMessage());

            return null;
        }
    }

}
