package org.occidere.githubnotifier.configuration

import org.occidere.githubnotifier.batch.GithubRepositoryNotificationTasklet
import org.springframework.batch.core.configuration.annotation.{JobBuilderFactory, JobScope, StepBuilderFactory, StepScope}
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.{Job, Step}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Bean, ComponentScan, Configuration}

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020. 08. 16.
 */
@Autowired
@Configuration
@ComponentScan(basePackages = Array("org.occidere.githubnotifier"))
class GithubRepositoryNotificationJobConfiguration(jobBuilderFactory: JobBuilderFactory, stepBuilderFactory: StepBuilderFactory) {
  @Bean
  def githubRepositoryNotificationJob: Job = jobBuilderFactory.get("githubRepositoryNotificationJob")
    .incrementer(new RunIdIncrementer)
    .start(githubRepositoryNotificationStep)
    .build

  @Bean
  @JobScope
  def githubRepositoryNotificationStep: Step = stepBuilderFactory.get("githubRepositoryNotificationStep")
    .tasklet(githubRepositoryNotificationTasklet)
    .build

  @Bean
  @StepScope
  def githubRepositoryNotificationTasklet = new GithubRepositoryNotificationTasklet
}