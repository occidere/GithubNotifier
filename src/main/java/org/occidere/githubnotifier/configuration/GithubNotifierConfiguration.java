package org.occidere.githubnotifier.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.http.impl.client.HttpClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.occidere.githubnotifier.api.GithubApi;
import org.occidere.githubnotifier.api.GithubFollowerApi;
import org.occidere.githubnotifier.batch.GithubUserFollowerReader;
import org.occidere.githubnotifier.batch.GithubUserNotificationWriter;
import org.occidere.githubnotifier.vo.GithubUser;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author occidere
 * @since 2019-11-29
 */

@Configuration
@EnableBatchProcessing
@EnableElasticsearchRepositories
@RequiredArgsConstructor
@ComponentScan(basePackages = "org.occidere.githubnotifier")
public class GithubNotifierConfiguration extends AbstractElasticsearchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Override
    public RestHighLevelClient elasticsearchClient() {
        return RestClients.create(ClientConfiguration.create("localhost:9200")).rest();
    }

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(2000);
        httpRequestFactory.setReadTimeout(3000);
        httpRequestFactory.setHttpClient(
            HttpClientBuilder.create()
                .setMaxConnTotal(100)
                .setMaxConnPerRoute(10)
                .build());
        return new RestTemplate(httpRequestFactory);
    }

    @Bean
    public GithubApi followerApi() {
        return new GithubFollowerApi();
    }

    @Bean
    public Job githubUserFollowerNotificationJob() {
        return jobBuilderFactory.get("githubUserFollowerNotificationJob")
            .incrementer(new RunIdIncrementer())
            .start(githubUserFollowerNotificationStep())
            .build();
    }

    @Bean
    public Step githubUserFollowerNotificationStep() {
        return stepBuilderFactory.get("githubUserFollowerNotificationStep")
            .<GithubUser, GithubUser>chunk(1)
            .reader(githubUserFollowerReader())
            .writer(githubUserNotificationWriter())
            .build();
    }

    @Bean
    public GithubUserFollowerReader githubUserFollowerReader() {
        return new GithubUserFollowerReader();
    }

    @Bean
    public GithubUserNotificationWriter githubUserNotificationWriter() {
        return new GithubUserNotificationWriter();
    }


}
