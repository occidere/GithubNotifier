package org.occidere.githubnotifier.batch

import org.occidere.githubnotifier.batch.GithubRepositoryNotificationTasklet.log
import org.occidere.githubnotifier.service.{GithubApiService, GithubRepoRepository, LineMessengerService}
import org.occidere.githubnotifier.vo.{GithubRepository, GithubRepositoryDiff}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.core.{ExitStatus, StepContribution, StepExecution, StepExecutionListener}
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020. 08. 16.
 */
object GithubRepositoryNotificationTasklet {
  val log: Logger = LoggerFactory.getLogger(classOf[GithubRepositoryNotificationTasklet])
}

class GithubRepositoryNotificationTasklet extends Tasklet with StepExecutionListener {
  @Autowired
  var apiService: GithubApiService = _

  @Autowired
  var repoRepository: GithubRepoRepository = _

  @Autowired
  var lineMessengerService: LineMessengerService = _

  private var userId: String = ""

  // TODO: 완성
  override def execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus = {
    log.info(s"User ID: $userId")

    val latestRepos = apiService.getRepositories(userId).map(fillLoginsIntoRepos)
    log.info(s"The number of repositories from API: ${latestRepos.size}")

    val prevRepos = repoRepository.findAllByOwnerLogin(userId)
      .groupMap(_.id)(x => x)
      .view
      .mapValues(_.head)
      .toMap
    log.info(s"The number of repositories from ES: ${prevRepos.size}")

    latestRepos.foreach(latestRepo => {
      val diff = new GithubRepositoryDiff(prevRepos(latestRepo.id), latestRepo)
      log.info(s"${latestRepo.name}, (changed: ${diff.hasNewChanged || diff.hasDeletedChanged})")

      // Send message
      lineMessengerService.sendRepositoryMessageIfExist(diff)
    })

    latestRepos.foreach(latestRepo => repoRepository.save(latestRepo))
//    repoRepository.saveAll(latestRepos)

    RepeatStatus.FINISHED
  }


  // FIXME
  private def fillLoginsIntoRepos(repo: GithubRepository): GithubRepository = {
    repo.forksLogin = apiService.getForksLogins(repo.ownerLogin, repo.name)
    repo.watchersLogin = apiService.getWatchersLogins(repo.ownerLogin, repo.name)
    repo.stargazersLogin = apiService.getStargazersLogins(repo.ownerLogin, repo.name)
    repo
  }

  override def beforeStep(stepExecution: StepExecution): Unit = userId = stepExecution.getJobParameters.getString("userId")

  override def afterStep(stepExecution: StepExecution): ExitStatus = ExitStatus.COMPLETED
}
