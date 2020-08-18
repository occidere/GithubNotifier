package org.occidere.githubnotifier.vo

import lombok.ToString

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020. 08. 15.
 */
@ToString
class GithubFollowerDiff(previousFollowers: Map[String, GithubFollower], latestFollowers: Map[String, GithubFollower]) {
  // Grouping followers by NEW / DELETE / NOT_CHANGED

  // New followers: followers who in latest follower list are not in previous one.
  val notChangedFollowers: Iterable[GithubFollower] = latestFollowers
    .filter(latest => previousFollowers.contains(latest._1))
    .values

  // Not changed followers: followers who in both previous and latest follower list.
  val newFollowers: Iterable[GithubFollower] = latestFollowers
    .filter(latest => !previousFollowers.contains(latest._1))
    .values

  // Deleted followers: followers who in previous follower list are not in latest one.
  val deletedFollowers: Iterable[GithubFollower] = previousFollowers
    .filter(prev => !latestFollowers.contains(prev._1))
    .values
}
