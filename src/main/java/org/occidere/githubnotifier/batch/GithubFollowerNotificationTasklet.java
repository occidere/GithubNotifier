package org.occidere.githubnotifier.batch;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.occidere.githubnotifier.service.GithubApiRepository;
import org.occidere.githubnotifier.service.GithubUserRepository;
import org.occidere.githubnotifier.vo.GithubFollower;
import org.occidere.githubnotifier.vo.GithubUser;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author sungjun.lee (occidere)
 * @since 2019-12-04
 */
@Slf4j
public class GithubFollowerNotificationTasklet implements Tasklet {

    @Autowired
    private GithubApiRepository apiRepository;

    @Autowired
    private GithubUserRepository userRepository;

    @Autowired
    private LineMessagingClient lineMessagingClient;

    @Value("#{jobParameters[botId]}")
    private String botId;

    @Value("#{jobParameters[userId]}")
    private String userId;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("User id: {}", userId);

        // Data from Github API
        GithubUser latestUser = apiRepository.getUser(userId);
        Map<String, GithubFollower> latestFollowers = apiRepository.getFollowers(userId).stream()
                .collect(Collectors.toMap(GithubFollower::getLogin, Function.identity()));

        log.info("The number of follower from API: {}", latestFollowers.size());

        // Data from DB (Elasticsearch)
        GithubUser previousUser = ObjectUtils.firstNonNull(userRepository.findByLogin(userId), new GithubUser());
        Map<String, GithubFollower> previousFollowers = previousUser.getFollowerList().stream()
                .collect(Collectors.toMap(GithubFollower::getLogin, Function.identity()));

        log.info("The number of follower stored in DB: {}", previousFollowers.size());

        // Grouping followers by NEW / DELETE / NOT_CHANGED
        final List<GithubFollower> newFollowers = new ArrayList<>();
        final List<GithubFollower> deletedFollowers = new ArrayList<>();
        final List<GithubFollower> notChangedFollowers = new ArrayList<>();

        // New followers: followers who in latest follower list are not in previous one.
        // Not changed followers: followers who in both previous and latest follower list.
        for (Entry<String, GithubFollower> entry : latestFollowers.entrySet()) {
            if (previousFollowers.containsKey(entry.getKey())) {
                notChangedFollowers.add(entry.getValue());
            } else {
                newFollowers.add(entry.getValue());
            }
        }

        // Deleted followers: followers who in previous follower list are not in latest one.
        for (Entry<String, GithubFollower> entry : previousFollowers.entrySet()) {
            if (!latestFollowers.containsKey(entry.getKey())) {
                deletedFollowers.add(entry.getValue());
            }
        }

        log.info("Recently added new followers: {}", newFollowers.size());
        log.info("Recently deleted followers: {}", deletedFollowers.size());
        log.info("Not changed followers: {}", notChangedFollowers.size());

        // Send Line message if exist added / deleted followers
        String msg = "";
        if (CollectionUtils.isNotEmpty(newFollowers)) {
            msg += String.format("[%d 명의 새로 추가된 팔로워]\n%s\n",
                    newFollowers.size(), newFollowers.stream().map(GithubFollower::getLogin).collect(Collectors.joining("\n")));
        }

        if (CollectionUtils.isNotEmpty(deletedFollowers)) {
            msg += String.format("[%d 명의 삭제된 팔로워]\n%s\n",
                    deletedFollowers.size(), deletedFollowers.stream().map(GithubFollower::getLogin).collect(Collectors.joining("\n")));
        }

        if (StringUtils.isNotBlank(msg)) {
            try {
                BotApiResponse response = lineMessagingClient.pushMessage(new PushMessage(botId, new TextMessage(msg))).get();
                log.info("{}", response);
            } catch (InterruptedException | ExecutionException e) {
                log.error("Fail to send push message!", e);
            }
        }

        // Update followers to latest status (new followers + exists follower)
        latestUser.setFollowerList(ListUtils.union(newFollowers, notChangedFollowers));
        userRepository.save(latestUser); // always update for user info changed situation

        return RepeatStatus.FINISHED;
    }
}
