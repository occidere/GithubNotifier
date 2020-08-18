//package org.occidere.githubnotifier.configuration;
//
//import lombok.RequiredArgsConstructor;
//import org.occidere.githubnotifier.batch.GithubRepositoryNotificationTasklet;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.JobScope;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author occidere
// * @Blog: https://occidere.blog.me
// * @Github: https://github.com/occidere
// * @since 2020-04-06
// */
//@Configuration
//@EnableBatchProcessing
//@RequiredArgsConstructor
//@ComponentScan(basePackages = "org.occidere.githubnotifier")
//public class GithubRepositoryNotificationJobConfiguration {
//
//    private final JobBuilderFactory jobBuilderFactory;
//    private final StepBuilderFactory stepBuilderFactory;
//
//    @Bean
//    public Job githubRepositoryNotificationJob() {
//        return jobBuilderFactory.get("githubRepositoryNotificationJob")
//                .incrementer(new RunIdIncrementer())
//                .start(githubRepositoryNotificationStep())
//                .build();
//    }
//
//    @Bean
//    @JobScope
//    public Step githubRepositoryNotificationStep() {
//        return stepBuilderFactory.get("githubRepositoryNotificationStep")
//                .tasklet(githubRepositoryNotificationTasklet())
//                .build();
//    }
//
//    @Bean
//    @StepScope
//    public GithubRepositoryNotificationTasklet githubRepositoryNotificationTasklet() {
//        return new GithubRepositoryNotificationTasklet();
//    }
//}
