package com.ln.tms.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.LBHttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

import java.net.MalformedURLException;

@Configuration
@EnableSolrRepositories(multicoreSupport = true)
public class SolrConfig {

    @Value("${solr.host}")
    private String solrHost;

    @Bean
    public SolrClient solrServer() throws MalformedURLException {
        return new LBHttpSolrClient(solrHost);
    }

}
