package org.occidere.githubnotifier.service

import org.occidere.githubnotifier.vo.{GithubFollower, GithubRepository, GithubUser}

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020. 08. 15.
 */
trait GithubApiService {
  val GITHUB_API_URL = "https://api.github.com"

  def getUser(login: String): GithubUser

  def getFollowers(login: String): Iterable[GithubFollower]

  def getRepositories(login: String): Iterable[GithubRepository]

  def getStargazersLogins(login: String, repoName: String): Iterable[String]

  def getWatchersLogins(login: String, repoName: String): Iterable[String]

  def getForksLogins(login: String, repoName: String): Iterable[String]
}
