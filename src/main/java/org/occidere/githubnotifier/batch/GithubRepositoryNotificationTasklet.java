package org.occidere.githubnotifier.batch;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.occidere.githubnotifier.service.GithubApiRepository;
import org.occidere.githubnotifier.service.GithubRepoRepository;
import org.occidere.githubnotifier.vo.GithubRepository;
import org.occidere.githubnotifier.vo.GithubRepositoryDiff;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author occidere
 * @since 2020. 03. 31.
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
@Slf4j
public class GithubRepositoryNotificationTasklet implements Tasklet {

    @Autowired
    private GithubApiRepository apiRepository;

    @Autowired
    private GithubRepoRepository repoRepository;

    @Autowired
    private LineMessagingClient lineMessagingClient;

    @Value("${line.bot.id}")
    private String botId;

    @Value("#{jobParameters[userId]}")
    private String userId;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        log.info("User ID: {}", userId);

        // Latest repos info from API
        List<GithubRepository> latestRepos = apiRepository.getRepositories(userId).stream()
                .map(this::fillLoginsIntoRepos)
                .collect(Collectors.toList());
        log.info("The number of repositories from API: {}", latestRepos.size());

        // Previous repos info from ES
        Map<String, GithubRepository> prevRepos = repoRepository.findAllByOwnerLogin(userId).stream()
                .collect(Collectors.toMap(GithubRepository::getId, Function.identity()));
        log.info("The number of repositories from ES: {}", prevRepos.size());

        // Find differences
        for (GithubRepository latestRepo : latestRepos) {
            GithubRepositoryDiff diff = new GithubRepositoryDiff(prevRepos.get(latestRepo.getId()), latestRepo);
            log.info("{}, (changed: {})", latestRepo.getName(), diff.isNewChanged() || diff.isDeletedChanged());

            // Send message
            if (diff.isNewChanged() || diff.isDeletedChanged()) {
                try {
                    String msg = buildMessage(diff);
                    BotApiResponse response = lineMessagingClient.pushMessage(new PushMessage(botId, new TextMessage(msg))).get();
                    log.info("{}", response);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Fail to send push message!", e);
                }
            }
        }

        repoRepository.saveAll(latestRepos);

        return RepeatStatus.FINISHED;
    }

    private GithubRepository fillLoginsIntoRepos(GithubRepository repo) {
        repo.setForksLogin(apiRepository.getForksLogins(repo.getOwnerLogin(), repo.getName()));
        repo.setWatchersLogin(apiRepository.getWatchersLogins(repo.getOwnerLogin(), repo.getName()));
        repo.setStargazersLogin(apiRepository.getStargazersLogins(repo.getOwnerLogin(), repo.getName()));
        return repo;
    }

    private String buildMessage(GithubRepositoryDiff diff) {
        StringBuilder msg = new StringBuilder("[" + diff.getName() + "]\n");
        final int indent = 4;
        if (diff.isNewChanged()) {
            msg.append("[신규]\n")
                    .append(CollectionUtils.isEmpty(diff.getNewStargazersLogin()) ? "" :
                            "- STAR\n" + toListedString(diff.getNewStargazersLogin(), indent) + "\n")
                    .append(CollectionUtils.isEmpty(diff.getNewForksLogin()) ? "" :
                            "- FORK\n" + toListedString(diff.getNewForksLogin(), indent) + "\n")
                    .append(CollectionUtils.isEmpty(diff.getNewWatchersLogin()) ? "" :
                            "- WATCH\n" + toListedString(diff.getNewWatchersLogin(), indent) + "\n");
        }
        if (diff.isDeletedChanged()) {
            msg.append("[삭제]\n")
                    .append(CollectionUtils.isEmpty(diff.getDeletedStargazersLogin()) ? "" :
                            "- STAR\n" + toListedString(diff.getDeletedStargazersLogin(), indent) + "\n")
                    .append(CollectionUtils.isEmpty(diff.getDeletedForksLogin()) ? "" :
                            "- FORK\n" + toListedString(diff.getDeletedForksLogin(), indent) + "\n")
                    .append(CollectionUtils.isEmpty(diff.getDeletedWatchersLogin()) ? "" :
                            "- WATCH\n" + toListedString(diff.getDeletedWatchersLogin(), indent) + "\n");
        }
        return msg.toString();
    }

    private String toListedString(List<String> list, int indent) {
        final String prefix = " ".repeat(Math.max(0, indent)) + "- ";
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(prefix).append(s).append("\n");
        }
        return sb.toString();
    }
}
