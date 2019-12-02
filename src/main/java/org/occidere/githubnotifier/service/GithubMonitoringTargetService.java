package org.occidere.githubnotifier.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.occidere.githubnotifier.vo.GithubMonitoringTarget;
import org.springframework.stereotype.Service;

/**
 * @author occidere
 * @since 2019. 12. 02.
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
@Service
@RequiredArgsConstructor
public class GithubMonitoringTargetService {

    private final GithubMonitoringTargetRepository repository;

    public void saveAll(Iterable<GithubMonitoringTarget> entities) {
        repository.saveAll(entities);
    }

    public List<String> findAllLogins() {
        List<String> logins = new ArrayList<>();
        repository.findAll().forEach(githubMonitoringTarget -> logins.add(githubMonitoringTarget.getLogin()));
        return logins;
    }
}
