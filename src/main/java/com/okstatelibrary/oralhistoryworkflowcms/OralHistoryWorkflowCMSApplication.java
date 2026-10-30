package com.okstatelibrary.oralhistoryworkflowcms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * Oral History Work flow CMS application. 
 */
@EnableCaching
@SpringBootApplication
@EnableElasticsearchRepositories(
        basePackages = "com.okstatelibrary.oralhistoryworkflowcms.repository"
)
public class OralHistoryWorkflowCMSApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                OralHistoryWorkflowCMSApplication.class, args);
    }
}

