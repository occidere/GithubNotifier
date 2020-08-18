package org.occidere.githubnotifier.batch

import org.occidere.githubnotifier.batch.GithubFollowerNotificationTasklet.log
import org.occidere.githubnotifier.service.{GithubApiService, GithubUserRepository, LineMessengerService}
import org.occidere.githubnotifier.vo.{GithubFollower, GithubFollowerDiff, GithubUser}
import org.slf4j.LoggerFactory
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.core.{ExitStatus, StepContribution, StepExecution, StepExecutionListener}
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.{Autowired, Value}


/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020-08-15
 */
object GithubFollowerNotificationTasklet {
  private val log = LoggerFactory.getLogger(classOf[GithubFollowerNotificationTasklet])
}

class GithubFollowerNotificationTasklet extends Tasklet with StepExecutionListener {
  @Autowired
  private var apiService: GithubApiService = _

  @Autowired
  private var userRepository: GithubUserRepository = _

  @Autowired
  private var lineMessengerService: LineMessengerService = _

//  @Value("#{jobParameters[userId]}")
  private var userId: String = _

  override def execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus = {
    log.info("User id: {}", userId)

    // Data from Github API
    val latestUser = apiService.getUser(userId)
    val latestFollowers: Map[String, GithubFollower] = createFollowerByLoginMap(apiService.getFollowers(userId))
    log.info(s"The number of follower from API: ${latestFollowers.size}")

    // Data from DB (Elasticsearch)
    val previousUser = Option(userRepository.findByLogin(userId)).fold(new GithubUser)(x => x)
    val previousFollowers = createFollowerByLoginMap(previousUser.followerList)
    log.info(s"The number of follower stored in DB: ${previousFollowers.size}")

    val followerDiff = new GithubFollowerDiff(previousFollowers, latestFollowers)
    log.info(s"Recently added new followers: ${followerDiff.newFollowers.size}")
    log.info(s"Recently deleted followers: ${followerDiff.deletedFollowers.size}")
    log.info(s"Not changed followers: ${followerDiff.notChangedFollowers.size}")

    // Send Line message if exist added / deleted followers
    lineMessengerService.sendFollowerMessageIfExist(followerDiff)

    // Update DB to latest data
    updateLatestData(previousUser, latestUser, followerDiff)

    RepeatStatus.FINISHED
  }

  // FIXME
  private def createFollowerByLoginMap(followers: Iterable[GithubFollower]): Map[String, GithubFollower] = followers.groupBy(_.login)
    .view
    .mapValues(_.head)
    .toMap

  private def updateLatestData(previousUser: GithubUser, latestUser: GithubUser, githubFollowerDiff: GithubFollowerDiff): Unit = {
    // Update followers to latest status (new followers + exists follower)
    latestUser.followerList = githubFollowerDiff.newFollowers ++ githubFollowerDiff.notChangedFollowers

    // Set repositories using previous one because of the information of repositories cannot fetch from Github API!
    latestUser.repositories = previousUser.repositories

    // Always update for user info changed situation
    userRepository.save(latestUser)
  }

  override def beforeStep(stepExecution: StepExecution): Unit = userId = stepExecution.getJobParameters.getString("userId")

  override def afterStep(stepExecution: StepExecution): ExitStatus = ExitStatus.COMPLETED
}
