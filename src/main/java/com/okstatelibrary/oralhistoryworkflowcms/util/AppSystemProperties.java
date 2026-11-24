package com.okstatelibrary.oralhistoryworkflowcms.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
//@ConfigurationProperties
//@PropertySource("classpath:application.properties")
public class AppSystemProperties {

    @Value("${spring.elasticsearch.uris}")
    private String elasticsearchUrl;

    public String getElasticsearchUrl() {
        return elasticsearchUrl;
    }
}