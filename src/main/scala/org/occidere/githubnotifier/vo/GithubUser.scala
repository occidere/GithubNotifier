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
  indexName = "github-users",
  shards = 5,
  replicas = 1,
  refreshInterval = "60s",
  createIndex = true,
)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class GithubUser {
  @Id
  var id: Long = _
  @JsonProperty("node_id")
  var nodeId: String = _
  var login: String = _
  var name: String = _
  var company: String = _
  var blog: String = _
  var location: String = _
  var email: String = _
  var bio: String = _
  @JsonProperty("avatar_url")
  var avatarUrl: String = _
  @JsonProperty("html_url")
  var htmlUrl: String = _
  var followerList: Iterable[GithubFollower] = mutable.ListBuffer[GithubFollower]() // FIXME
  var repositories: Iterable[GithubRepository] = mutable.ListBuffer[GithubRepository]() // FIXME
}
