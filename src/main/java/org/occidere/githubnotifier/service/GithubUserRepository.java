package org.occidere.githubnotifier.service;

import org.occidere.githubnotifier.vo.GithubUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author occidere
 * @since 2019-11-29
 */
public interface GithubUserRepository extends ElasticsearchRepository<GithubUser, GithubUser> {

}
