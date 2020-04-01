package org.occidere.githubnotifier.configuration;

import lombok.RequiredArgsConstructor;
import org.occidere.githubnotifier.batch.GithubFollowerNotificationTasklet;
import org.occidere.githubnotifier.batch.GithubRepositoryNotificationTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
    public Job githubUserNotificationJob() {
        return jobBuilderFactory.get("githubUserNotificationJob")
                .incrementer(new RunIdIncrementer())
                .start(githubFollowerNotificationStep())
                .next(githubRepositoryNotificationStep())
                .build();
    }

    @Bean
    @JobScope
    public Step githubFollowerNotificationStep() {
        return stepBuilderFactory.get("githubFollowerNotificationStep")
                .tasklet(githubFollowerNotificationTasklet())
                .build();
    }

    @Bean
    @JobScope
    public Step githubRepositoryNotificationStep() {
        return stepBuilderFactory.get("githubRepositoryNotificationStep")
                .tasklet(githubRepositoryNotificationTasklet())
                .build();
    }

    @Bean
    @StepScope
    public GithubFollowerNotificationTasklet githubFollowerNotificationTasklet() {
        return new GithubFollowerNotificationTasklet();
    }

    @Bean
    @StepScope
    public GithubRepositoryNotificationTasklet githubRepositoryNotificationTasklet() {
        return new GithubRepositoryNotificationTasklet();
    }
}
