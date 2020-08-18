package org.occidere.githubnotifier.vo

import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonInclude, JsonProperty}
import lombok.ToString

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020. 08. 15.
 */
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class GithubFollower {
  val login: String = null
  val id: Long = -1
  @JsonProperty("node_id")
  val nodeId: String = null
  @JsonProperty("avatar_url")
  val avatarUrl: String = null
  @JsonProperty("html_url")
  val htmlUrl: String = null
  val `type`: String = null // FIXME
}
