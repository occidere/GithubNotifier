package org.occidere.githubnotifier.service

import org.occidere.githubnotifier.vo.GithubRepository
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020. 08. 16.
 */
@Repository
trait GithubRepoRepository extends ElasticsearchRepository[GithubRepository, String] {
  def findAllByOwnerLogin(login: String): Iterable[GithubRepository]

//  def saveAll(githubRepositories: Iterable[GithubRepository]): Unit
}
