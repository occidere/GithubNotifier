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
            msg += String.format("[%d 명의 새로 추가된 팔로워]\n%s\n",
                    newFollowers.size(), newFollowers.stream().map(GithubFollower::getLogin).collect(Collectors.joining("\n")));
        }

        if (CollectionUtils.isNotEmpty(deletedFollowers)) {
            msg += String.format("[%d 명의 삭제된 팔로워]\n%s\n",
                    deletedFollowers.size(), deletedFollowers.stream().map(GithubFollower::getLogin).collect(Collectors.joining("\n")));
        }

        return msg;
    }

    private String createRepositoryMessage(GithubRepositoryDiff diff) {
        final int indent = 4;
        StringBuilder msg = new StringBuilder("[" + diff.getName() + "]\n");

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

    private static String toListedString(List<String> list, int indent) {
        final String prefix = " ".repeat(Math.max(0, indent)) + "- ";
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(prefix).append(s).append("\n");
        }
        return sb.toString();
    }
}
