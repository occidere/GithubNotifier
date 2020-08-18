package org.occidere.githubnotifier.service

import org.occidere.githubnotifier.vo.GithubUser
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020. 08. 15.
 */
trait GithubUserRepository extends ElasticsearchRepository[GithubUser, String] {
  def findByLogin(login: String): GithubUser
}
