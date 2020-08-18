package org.occidere.githubnotifier.configuration

import org.occidere.githubnotifier.batch.GithubFollowerNotificationTasklet
import org.springframework.batch.core.configuration.annotation._
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
@EnableBatchProcessing
@ComponentScan(basePackages = Array("org.occidere.githubnotifier"))
class GithubFollowerNotificationJobConfiguration(jobBuilderFactory: JobBuilderFactory, stepBuilderFactory: StepBuilderFactory) {
  @Bean
  def githubFollowerNotificationJob: Job = jobBuilderFactory.get("githubFollowerNotificationJob")
    .incrementer(new RunIdIncrementer())
    .start(githubFollowerNotificationStep)
    .build()

  @Bean
  @JobScope
  def githubFollowerNotificationStep: Step = stepBuilderFactory.get("githubFollowerNotificationStep")
    .tasklet(githubFollowerNotificationTasklet)
    .build()

  @Bean
  @StepScope
  def githubFollowerNotificationTasklet = new GithubFollowerNotificationTasklet()
}
