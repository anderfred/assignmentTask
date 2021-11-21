package com.anderfred.assignmenttask.configuration;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/***
 *      Configuration for elastic search
 *      Dividing jpa repos from elastic search
 *      Also setting default client - RestHighLevelClient
 */

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.anderfred.assignmenttask.elasticrepo")
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {

        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("es01:9200")
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
}