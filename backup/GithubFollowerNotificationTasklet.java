//package org.occidere.githubnotifier.batch;
//
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.ListUtils;
//import org.apache.commons.lang3.ObjectUtils;
//import org.occidere.githubnotifier.service.GithubApiService;
//import org.occidere.githubnotifier.service.GithubUserRepository;
//import org.occidere.githubnotifier.service.LineMessengerService;
//import org.occidere.githubnotifier.vo.GithubFollower;
//import org.occidere.githubnotifier.vo.GithubFollowerDiff;
//import org.occidere.githubnotifier.vo.GithubUser;
//import org.springframework.batch.core.StepContribution;
//import org.springframework.batch.core.scope.context.ChunkContext;
//import org.springframework.batch.core.step.tasklet.Tasklet;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//
///**
// * @author occidere
// * @Blog: https://blog.naver.com/occidere
// * @Github: https://github.com/occidere
// * @since 2019-12-04
// */
//@Slf4j
//public class GithubFollowerNotificationTasklet implements Tasklet {
//
//    @Autowired
//    private GithubApiService apiService;
//
//    @Autowired
//    private GithubUserRepository userRepository;
//
//    @Autowired
//    private LineMessengerService lineMessengerService;
//
//    @Value("#{jobParameters[userId]}")
//    private String userId;
//
//    @Override
//    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
//        log.info("User id: {}", userId);
//
//        // Data from Github API
//        GithubUser latestUser = apiService.getUser(userId);
//        Map<String, GithubFollower> latestFollowers = createFollowerByLoginMap(apiService.getFollowers(userId));
//        log.info("The number of follower from API: {}", latestFollowers.size());
//
//        // Data from DB (Elasticsearch)
//        GithubUser previousUser = ObjectUtils.firstNonNull(userRepository.findByLogin(userId), new GithubUser());
//        Map<String, GithubFollower> previousFollowers = createFollowerByLoginMap(previousUser.getFollowerList());
//        log.info("The number of follower stored in DB: {}", previousFollowers.size());
//
//        GithubFollowerDiff followerDiff = new GithubFollowerDiff(previousFollowers, latestFollowers);
//        log.info("Recently added new followers: {}", followerDiff.getNewFollowers().size());
//        log.info("Recently deleted followers: {}", followerDiff.getDeletedFollowers().size());
//        log.info("Not changed followers: {}", followerDiff.getNotChangedFollowers().size());
//
//        // Send Line message if exist added / deleted followers
//        lineMessengerService.sendFollowerMessageIfExist(followerDiff);
//
//        // Update DB to latest data
//        updateLatestData(previousUser, latestUser, followerDiff);
//
//        return RepeatStatus.FINISHED;
//    }
//
//    private Map<String, GithubFollower> createFollowerByLoginMap(final List<GithubFollower> followers) {
//        return followers.stream().collect(Collectors.toMap(GithubFollower::getLogin, Function.identity()));
//    }
//
//    private void updateLatestData(GithubUser previousUser, GithubUser latestUser, GithubFollowerDiff githubFollowerDiff) {
//        // Update followers to latest status (new followers + exists follower)
//        latestUser.setFollowerList(ListUtils.union(githubFollowerDiff.getNewFollowers(), githubFollowerDiff.getNotChangedFollowers()));
//
//        // Set repositories using previous one because of the information of repositories cannot fetch from Github API!
//        latestUser.setRepositories(previousUser.getRepositories());
//
//        // Always update for user info changed situation
//        userRepository.save(latestUser);
//    }
//}
