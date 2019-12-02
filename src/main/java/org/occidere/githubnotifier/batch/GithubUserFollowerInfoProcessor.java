package org.occidere.githubnotifier.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.occidere.githubnotifier.service.GithubApiRepository;
import org.occidere.githubnotifier.service.GithubUserRepository;
import org.occidere.githubnotifier.vo.GithubUser;
import org.occidere.githubnotifier.vo.GithubUserFollowerInfo;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author occidere
 * @since 2019. 12. 02.
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
@Slf4j
public class GithubUserFollowerInfoProcessor implements ItemProcessor<String, GithubUserFollowerInfo>, StepExecutionListener {

    private static final int NEW_FOLLOWER = 1;
    private static final int DELETED_FOLLOWER = -1;
    private static final int NOT_CHANGED_FOLLOWER = 0;

    @Autowired
    private GithubApiRepository apiRepository;

    @Autowired
    private GithubUserRepository userRepository;

    @Override
    public void beforeStep(StepExecution stepExecution) {
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    @Override
    public GithubUserFollowerInfo process(String item) throws Exception {
        System.out.println("USER: " + item);

        // Data from Github API
        GithubUser latestUser = apiRepository.getUser(item);
        final Map<String, Integer> latestFollowers = apiRepository.getFollowers(item).stream()
                .collect(Collectors.toMap(Function.identity(), id -> NEW_FOLLOWER));
        log.info("{} info from Github API: {}", item, latestUser);
        log.info("Followers of {}: {}", item, latestFollowers);


        // Data from DB (Elasticsearch)
        GithubUser previousUser = ObjectUtils.firstNonNull(userRepository.findByLogin(item), new GithubUser());
        final Map<String, Integer> previousFollowers = previousUser.getFollowerList().stream()
                .collect(Collectors.toMap(Function.identity(), id -> NOT_CHANGED_FOLLOWER));
        log.info("{} info from DB: {}", item, previousUser);
        log.info("Followers of {}: {}", item, previousFollowers);


        // Grouping followers by NEW / DELETE / NOT_CHANGED
        final List<String> newFollowers = new ArrayList<>();
        final List<String> deletedFollowers = new ArrayList<>();
        final List<String> notChangedFollowers = new ArrayList<>();

        latestFollowers.forEach((k, v) -> previousFollowers.merge(k, v, Integer::sum));
        previousFollowers.forEach((userId, status) -> {
            switch (status) {
                case NOT_CHANGED_FOLLOWER:
                    notChangedFollowers.add(userId);
                    break;
                case DELETED_FOLLOWER:
                    deletedFollowers.add(userId);
                    break;
                case NEW_FOLLOWER:
                    newFollowers.add(userId);
                    break;
                default:
                    log.warn("Not supported type: {}", status);
            }
        });


        // Update followers to latest status (new followers + exists follower)
        latestUser.setFollowerList(ListUtils.union(newFollowers, notChangedFollowers));

        // Wrapping GithubUser info and followers info
        return GithubUserFollowerInfo.builder()
                .githubUser(latestUser)
                .newFollowers(newFollowers)
                .deletedFollowers(deletedFollowers)
                .notChangedFollowers(notChangedFollowers)
                .build();
    }
}
