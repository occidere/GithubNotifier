package org.occidere.githubnotifier.service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.occidere.githubnotifier.vo.GithubFollower;
import org.occidere.githubnotifier.vo.GithubFollowerDiff;
import org.occidere.githubnotifier.vo.GithubRepositoryDiff;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-04-14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LineMessengerServiceImpl implements LineMessengerService {

    private final LineMessagingClient lineMessagingClient;

    @Value("${line.bot.id}")
    private String botId;

    @Override
    public void sendFollowerMessageIfExist(GithubFollowerDiff githubFollowerDiff) {
        sendMessageIfExist(createFollowerMessage(githubFollowerDiff));
    }

    @Override
    public void sendRepositoryMessageIfExist(GithubRepositoryDiff githubRepositoryDiff) {
        if (githubRepositoryDiff.isNewChanged() || githubRepositoryDiff.isDeletedChanged()) {
            sendMessageIfExist(createRepositoryMessage(githubRepositoryDiff));
        }
    }

    private String createFollowerMessage(GithubFollowerDiff githubFollowerDiff) {
        String msg = "";

        final List<GithubFollower> newFollowers = githubFollowerDiff.getNewFollowers();
        final List<GithubFollower> deletedFollowers = githubFollowerDiff.getDeletedFollowers();

        if (CollectionUtils.isNotEmpty(newFollowers)) {
            msg += String.format("[%d of new followers]\n%s\n",
                newFollowers.size(),
                newFollowers.stream()
                    .map(GithubFollower::getLogin)
                    .sorted()
                    .collect(Collectors.joining("\n"))
            );
        }

        if (CollectionUtils.isNotEmpty(deletedFollowers)) {
            msg += String.format("[%d of deleted followers]\n%s\n",
                deletedFollowers.size(),
                deletedFollowers.stream()
                    .map(GithubFollower::getLogin)
                    .sorted()
                    .collect(Collectors.joining("\n"))
            );
        }

        return msg;
    }

    private String createRepositoryMessage(GithubRepositoryDiff diff) {
        final int indent = 4;
        StringBuilder msg = new StringBuilder("[" + diff.getName() + "]\n");

        if (diff.isNewChanged()) {
            msg.append("[NEW]\n")
                    .append(CollectionUtils.isEmpty(diff.getNewStargazersLogin()) ? "" :
                            "- STAR\n" + toSortedListString(diff.getNewStargazersLogin(), indent) + "\n")
                    .append(CollectionUtils.isEmpty(diff.getNewForksLogin()) ? "" :
                            "- FORK\n" + toSortedListString(diff.getNewForksLogin(), indent) + "\n")
                    .append(CollectionUtils.isEmpty(diff.getNewWatchersLogin()) ? "" :
                            "- WATCH\n" + toSortedListString(diff.getNewWatchersLogin(), indent) + "\n");
        }

        if (diff.isDeletedChanged()) {
            msg.append("[DELETED]\n")
                    .append(CollectionUtils.isEmpty(diff.getDeletedStargazersLogin()) ? "" :
                            "- STAR\n" + toSortedListString(diff.getDeletedStargazersLogin(), indent) + "\n")
                    .append(CollectionUtils.isEmpty(diff.getDeletedForksLogin()) ? "" :
                            "- FORK\n" + toSortedListString(diff.getDeletedForksLogin(), indent) + "\n")
                    .append(CollectionUtils.isEmpty(diff.getDeletedWatchersLogin()) ? "" :
                            "- WATCH\n" + toSortedListString(diff.getDeletedWatchersLogin(), indent) + "\n");
        }

        return msg.toString();
    }

    private void sendMessageIfExist(String msg) {
        if (StringUtils.isNotBlank(msg)) {
            try {
                BotApiResponse response = lineMessagingClient.pushMessage(new PushMessage(botId, new TextMessage(msg))).get();
                log.info("{}", response);
            } catch (InterruptedException | ExecutionException e) {
                log.error("Fail to send push message!", e);
            }
        }
    }

    private static String toSortedListString(final List<String> logins, final int indent) {
        final String prefix = " ".repeat(Math.max(0, indent)) + "- ";
        return logins.stream()
            .sorted()
            .map(login -> prefix + login + "\n")
            .collect(Collectors.joining());
    }
}
