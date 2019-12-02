package org.occidere.githubnotifier.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.http.impl.client.HttpClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.occidere.githubnotifier.batch.GithubUserFetchReader;
import org.occidere.githubnotifier.batch.GithubUserFollowerInfoProcessor;
import org.occidere.githubnotifier.batch.GithubUserReflectWriter;
import org.occidere.githubnotifier.service.GithubApiRepository;
import org.occidere.githubnotifier.service.GithubApiService;
import org.occidere.githubnotifier.vo.GithubUserFollowerInfo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
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
 * @since 2019. 11. 29.
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
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
    @StepScope
    public RestHighLevelClient elasticsearchClient() {
        return RestClients.create(ClientConfiguration.create("localhost:9200")).rest();
    }

    @Bean
    @StepScope
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
    @StepScope
    public GithubApiRepository githubApiRepository() {
        return new GithubApiService();
    }

    @Bean
    public Job githubUserFollowerNotificationJob() {
        return jobBuilderFactory.get("githubUserFollowerNotificationJob")
                .incrementer(new RunIdIncrementer())
                .start(githubUserFollowerNotificationStep())
                .build();
    }

    @Bean
    @JobScope
    public Step githubUserFollowerNotificationStep() {
        return stepBuilderFactory.get("githubUserFollowerNotificationStep")
                .<String, GithubUserFollowerInfo>chunk(1)
                .reader(getGithubUserFetchReader())
                .processor(getGithubUserFollowerInfoProcessor())
                .writer(getGithubUserReflectWriter())
                .build();
    }

    @Bean
    @StepScope
    public GithubUserFetchReader getGithubUserFetchReader() {
        return new GithubUserFetchReader();
    }

    @Bean
    @StepScope
    public ItemProcessor<String, GithubUserFollowerInfo> getGithubUserFollowerInfoProcessor() {
        return new GithubUserFollowerInfoProcessor();
    }

    @Bean
    @StepScope
    public ItemWriter<GithubUserFollowerInfo> getGithubUserReflectWriter() {
        return new GithubUserReflectWriter();
    }
}
