package org.occidere.githubnotifier.vo

import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonInclude, JsonProperty}
import lombok.ToString
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

import scala.collection.mutable

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020. 08. 15.
 */
@Document(
  indexName = "github-repos",
  shards = 5,
  replicas = 1,
  refreshInterval = "60s",
  createIndex = true,
)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class GithubRepository {
  @Id
  var id: String = _
  var name: String = _
  var ownerId: String = _
  var ownerLogin: String = _
  var description: String = _
  @JsonProperty("stargazers_count")
  var stargazersCount: Int = _
  @JsonProperty("watchers_count")
  var watchersCount: Int = _
  @JsonProperty("forks_count")
  var forksCount: Int = _

  // FIXME var
  var stargazersLogin: Iterable[String] = mutable.ListBuffer[String]()
  var watchersLogin: Iterable[String] = mutable.ListBuffer[String]()
  var forksLogin: Iterable[String] = mutable.ListBuffer[String]()

  @JsonProperty("ownser")
  private def unpackOwner(owner: Map[String, Any]): Unit = {
    ownerId = owner("id").toString
    ownerLogin = owner("login").toString
  }
}