package org.occidere.githubnotifier.service;

import org.occidere.githubnotifier.vo.GithubUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author occidere
 * @since 2019. 11. 29.
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
@Repository
public interface GithubUserRepository extends ElasticsearchRepository<GithubUser, String> {
    GithubUser findByLogin(String login);
}
