package org.occidere.githubnotifier.service;

import org.occidere.githubnotifier.vo.GithubMonitoringTarget;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author occidere
 * @since 2019. 12. 02.
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
@Repository
public interface GithubMonitoringTargetRepository extends ElasticsearchRepository<GithubMonitoringTarget, String> {
}
