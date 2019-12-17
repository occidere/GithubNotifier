package org.occidere.githubnotifier.configuration;

import com.linecorp.bot.client.LineMessagingClient;
import lombok.RequiredArgsConstructor;
import org.apache.http.impl.client.HttpClientBuilder;
import org.occidere.githubnotifier.batch.GithubFollowerNotificationTasklet;
import org.occidere.githubnotifier.service.GithubApiRepository;
import org.occidere.githubnotifier.service.GithubApiService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
@RequiredArgsConstructor
@ComponentScan(basePackages = "org.occidere.githubnotifier")
public class GithubNotifierConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


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
    @StepScope
    public LineMessagingClient lineMessagingClient(@Value("${line.channel.token}") String lineChannel) {
        return LineMessagingClient.builder(lineChannel).build();
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
                .tasklet(githubFollowerNotificationTasklet())
                .build();
    }

    @Bean
    @StepScope
    public GithubFollowerNotificationTasklet githubFollowerNotificationTasklet() {
        return new GithubFollowerNotificationTasklet();
    }
}
