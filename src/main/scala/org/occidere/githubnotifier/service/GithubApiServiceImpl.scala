package org.occidere.githubnotifier.service

import java.net.{HttpURLConnection, URI, URL, URLConnection}
import java.time.Duration
import java.util
import java.util.Properties
import java.util.concurrent.TimeUnit

import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.{ReadTimeoutHandler, WriteTimeoutHandler}
import lombok.Value
import org.apache.commons.lang3.StringUtils
import org.occidere.githubnotifier.service.GithubApiServiceImpl.MAPPER
import org.occidere.githubnotifier.vo.{GithubFollower, GithubRepository, GithubUser}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import org.springframework.http.HttpHeaders
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClient.{RequestHeadersSpec, RequestHeadersUriSpec}
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import reactor.netty.tcp.TcpClient
import reactor.util.retry.Retry

import scala.collection.mutable
import scala.jdk.CollectionConverters._

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020. 08. 15.
 */
object GithubApiServiceImpl {
  val log: Logger = LoggerFactory.getLogger(classOf[GithubApiServiceImpl])
  val MAPPER: ObjectMapper = new ObjectMapper()
}

@Service
@Autowired
class GithubApiServiceImpl extends GithubApiService {
  private val props: Properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("/application.properties"))

//  @Value("${github.api.token:}")
  private val githubApiToken: String = props.getProperty("github.api.token", "")

  val webClient: WebClient = WebClient.builder()
    .clientConnector(
      new ReactorClientHttpConnector(
        HttpClient.from(
          TcpClient.create()
//            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 60_000)
            .doOnConnected(conn => {
              conn.addHandlerLast(new ReadTimeoutHandler(60, TimeUnit.SECONDS))
              conn.addHandlerLast(new WriteTimeoutHandler(60, TimeUnit.SECONDS))
            })
        )
      )
    ).defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0")
    .build()

  override def getUser(login: String): GithubUser =
    MAPPER.convertValue(getRawData(s"/users/$login"), classOf[GithubUser])

  override def getFollowers(login: String): Iterable[GithubFollower] =
    getAllRawData(s"/users/$login/followers")
      .map(MAPPER.convertValue(_, classOf[GithubFollower]))

  override def getRepositories(login: String): Iterable[GithubRepository] =
    getAllRawData(s"/users/$login/repos")
      .map(MAPPER.convertValue(_, classOf[GithubRepository]))

  override def getStargazersLogins(login: String, repoName: String): Iterable[String] =
    getAllRawData(s"/repos/$login/$repoName/stargazers")
      .map(_ ("login").toString)

  override def getWatchersLogins(login: String, repoName: String): Iterable[String] =
    getAllRawData(s"/repos/$login/$repoName/watchers")
      .map(_ ("login").toString)

  override def getForksLogins(login: String, repoName: String): Iterable[String] =
    getAllRawData(s"/repos/$login/$repoName/forks")
      .map(_ ("owner").asInstanceOf[Map[String, Any]])
      .map(_ ("login").toString)

  private def getRawData(url: String): mutable.LinkedHashMap[String, Any] = getBody(url).head

  private def getAllRawData(url: String): List[mutable.LinkedHashMap[String, Any]] = Iterator.from(1)
    .map(page => getBody(s"$url?page=$page"))
    .takeWhile(_.nonEmpty)
    .reduceLeft((x, y) => x ++ y) // FIXME

//  private def getBody(uri: String): Mono[List[mutable.LinkedHashMap[String, Any]]] = webClient.get()
//    .uri(URI.create(s"$GITHUB_API_URL$uri"))
//    .asInstanceOf[RequestHeadersUriSpec[_]]
//    .headers(h => if (githubApiToken.nonEmpty) h.setBearerAuth(githubApiToken))
//    .asInstanceOf[RequestHeadersUriSpec[_]]
//    .retrieve()
//    .bodyToFlux(classOf[mutable.LinkedHashMap[String, Any]])
//    .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)))
//    .collectList()
//    .map(_.asScala.toList)
//    .defaultIfEmpty(List[mutable.LinkedHashMap[String, Any]]())

  private def getBody(uri: String): List[mutable.LinkedHashMap[String, Any]] = {
    val conn: URLConnection = new URL(s"$GITHUB_API_URL$uri").openConnection()
    conn.setRequestProperty("User-Agent", "Mozilla/5.0")
    if (githubApiToken.nonEmpty) conn.setRequestProperty("Authorization", s"Basic $githubApiToken")
    MAPPER.readValue(conn.getInputStream, classOf[mutable.ListBuffer[mutable.LinkedHashMap[String, Any]]]).toList // FIXME: ObjectMapper 매핑이 안되는지?
  }

}
