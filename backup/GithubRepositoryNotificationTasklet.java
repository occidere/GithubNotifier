//package org.occidere.githubnotifier.batch;
//
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//import lombok.extern.slf4j.Slf4j;
//import org.occidere.githubnotifier.service.GithubApiService;
//import org.occidere.githubnotifier.service.GithubRepoRepository;
//import org.occidere.githubnotifier.service.LineMessengerService;
//import org.occidere.githubnotifier.vo.GithubRepository;
//import org.occidere.githubnotifier.vo.GithubRepositoryDiff;
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
// * @since 2020. 03. 31.
// */
//@Slf4j
//public class GithubRepositoryNotificationTasklet implements Tasklet {
//
//    @Autowired
//    private GithubApiService apiService;
//
//    @Autowired
//    private GithubRepoRepository repoRepository;
//
//    @Autowired
//    private LineMessengerService lineMessengerService;
//
//    @Value("#{jobParameters[userId]}")
//    private String userId;
//
//    @Override
//    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
//        log.info("User ID: {}", userId);
//
//        // Latest repos info from API
//        List<GithubRepository> latestRepos = apiService.getRepositories(userId).stream()
//                .map(this::fillLoginsIntoRepos)
//                .collect(Collectors.toList());
//        log.info("The number of repositories from API: {}", latestRepos.size());
//
//        // Previous repos info from ES
//        Map<String, GithubRepository> prevRepos = repoRepository.findAllByOwnerLogin(userId).stream()
//                .collect(Collectors.toMap(GithubRepository::getId, Function.identity()));
//        log.info("The number of repositories from ES: {}", prevRepos.size());
//
//        // Find differences
//        for (GithubRepository latestRepo : latestRepos) {
//            GithubRepositoryDiff diff = new GithubRepositoryDiff(prevRepos.get(latestRepo.getId()), latestRepo);
//            log.info("{}, (changed: {})", latestRepo.getName(), diff.isNewChanged() || diff.isDeletedChanged());
//
//            // Send message
//            lineMessengerService.sendRepositoryMessageIfExist(diff);
//        }
//
//        repoRepository.saveAll(latestRepos);
//
//        return RepeatStatus.FINISHED;
//    }
//
//    private GithubRepository fillLoginsIntoRepos(GithubRepository repo) {
//        repo.setForksLogin(apiService.getForksLogins(repo.getOwnerLogin(), repo.getName()));
//        repo.setWatchersLogin(apiService.getWatchersLogins(repo.getOwnerLogin(), repo.getName()));
//        repo.setStargazersLogin(apiService.getStargazersLogins(repo.getOwnerLogin(), repo.getName()));
//        return repo;
//    }
//}
