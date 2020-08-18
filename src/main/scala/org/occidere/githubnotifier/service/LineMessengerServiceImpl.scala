package org.occidere.githubnotifier.service

import java.util.Properties

import com.linecorp.bot.client.LineMessagingClient
import com.linecorp.bot.model.PushMessage
import com.linecorp.bot.model.message.TextMessage
import com.linecorp.bot.model.response.BotApiResponse
import lombok.Value
import org.apache.commons.lang3.StringUtils
import org.occidere.githubnotifier.service.LineMessengerServiceImpl._
import org.occidere.githubnotifier.vo.{GithubFollowerDiff, GithubRepositoryDiff}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import org.springframework.stereotype.Service

import scala.util.{Failure, Success, Try}

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020. 08. 15.
 */
object LineMessengerServiceImpl {
  private val log = LoggerFactory.getLogger(classOf[LineMessengerServiceImpl])

  implicit class IndentUtils(it: Iterable[String]) {
    def toMessage(`type`: String, indent: Int = 4): String = {
      if (it.nonEmpty) {
        val prefix = " ".repeat(Math.max(0, indent)) + "- "
        s"- ${`type`}\n${it.toList.sorted.map(x => prefix + x + "\n").mkString("")}"
      } else ""
    }
  }

}

@Service
@Autowired
class LineMessengerServiceImpl(lineMessagingClient: LineMessagingClient) extends LineMessengerService {
  private val props: Properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("/application.properties"))

//  @Value("${line.bot.id}")
  private val botId: String = props.getProperty("line.bot.id", "")

  override def sendFollowerMessageIfExist(githubFollowerDiff: GithubFollowerDiff): Unit = sendMessageIfExist(createFollowerMessage(githubFollowerDiff))

  override def sendRepositoryMessageIfExist(githubRepositoryDiff: GithubRepositoryDiff): Unit = sendMessageIfExist(createRepositoryMessage(githubRepositoryDiff))

  private def createFollowerMessage(githubFollowerDiff: GithubFollowerDiff): String = {
    val msg: StringBuilder = new StringBuilder()

    val newFollowers = githubFollowerDiff.newFollowers
    if (newFollowers.nonEmpty) msg ++=
      s"""[${newFollowers.size} of new followers]
         |${newFollowers.map(_.login).toList.sorted.mkString("\n")}
         |""".stripMargin

    val deletedFollowers = githubFollowerDiff.deletedFollowers
    if (deletedFollowers.nonEmpty) msg ++=
      s"""[${deletedFollowers.size} of deleted followers]
         |${deletedFollowers.map(_.login).toList.sorted.mkString("\n")}
         |""".stripMargin

    msg.result()
  }

  private def createRepositoryMessage(diff: GithubRepositoryDiff): String = {
    val msg: StringBuilder = new StringBuilder()

    if (diff.hasNewChanged) msg ++=
      s"""[NEW]
         |${diff.newStargazersLogin.toMessage("- STAR")}
         |${diff.newForksLogin.toMessage("- FORK")}
         |${diff.newWatchersLogin.toMessage("- WATCH")}
         |""".stripMargin

    if (diff.hasDeletedChanged) msg ++=
      s"""[DELETED]
         |${diff.deletedStargazersLogin.toMessage("- STAR")}
         |${diff.deletedForksLogin.toMessage("- FORK")}
         |${diff.deletedWatchersLogin.toMessage("- WATCH")}
         |""".stripMargin

    msg.result()
  }

  def sendMessageIfExist(msg: String): Unit = if (StringUtils.isNotBlank(msg))
    Try {
      lineMessagingClient.pushMessage(new PushMessage(botId, new TextMessage(msg))).get()
    } match {
      case Success(response: BotApiResponse) => log.info(s"$response")
      case Failure(e) => log.error("Fail to send push message!", e)
    }
}