package org.occidere.githubnotifier.service

import org.occidere.githubnotifier.vo.{GithubFollowerDiff, GithubRepositoryDiff}

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020. 08. 15.
 */
trait LineMessengerService{
  def sendFollowerMessageIfExist(githubFollowerDiff: GithubFollowerDiff): Unit
  def sendRepositoryMessageIfExist(githubRepositoryDiff: GithubRepositoryDiff): Unit
}
