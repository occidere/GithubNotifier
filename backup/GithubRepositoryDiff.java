//package org.occidere.githubnotifier.vo;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import lombok.Getter;
//import lombok.ToString;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.collections4.ListUtils;
//
///**
// * @author occidere
// * @Blog: https://blog.naver.com/occidere
// * @Github: https://github.com/occidere
// * @since 2020. 03. 31.
// */
//@Getter
//@ToString
//public class GithubRepositoryDiff {
//
//    private final String name;
//    private final boolean isNewChanged;
//    private final boolean isDeletedChanged;
//
//    // New
//    private final List<String> newForksLogin;
//    private final List<String> newWatchersLogin;
//    private final List<String> newStargazersLogin;
//
//    // Deleted
//    private final List<String> deletedForksLogin;
//    private final List<String> deletedWatchersLogin;
//    private final List<String> deletedStargazersLogin;
//
//    public GithubRepositoryDiff(GithubRepository prevRepo, GithubRepository latestRepo) {
//        List<String> latestForksLogin = latestRepo.getForksLogin();
//        List<String> latestWatchersLogin = latestRepo.getWatchersLogin();
//        List<String> latestStargazersLogin = latestRepo.getStargazersLogin();
//
//        List<String> prevForksLogin = Objects.nonNull(prevRepo) ? prevRepo.getForksLogin() : new ArrayList<>();
//        List<String> prevWatchersLogin = Objects.nonNull(prevRepo) ? prevRepo.getWatchersLogin() : new ArrayList<>();
//        List<String> prevStargazersLogin = Objects.nonNull(prevRepo) ? prevRepo.getStargazersLogin() : new ArrayList<>();
//
//        // New
//        this.newForksLogin = ListUtils.subtract(latestForksLogin, prevForksLogin);
//        this.newWatchersLogin = ListUtils.subtract(latestWatchersLogin, prevWatchersLogin);
//        this.newStargazersLogin = ListUtils.subtract(latestStargazersLogin, prevStargazersLogin);
//        this.isNewChanged = CollectionUtils.isNotEmpty(this.newForksLogin) || CollectionUtils.isNotEmpty(this.newWatchersLogin) ||
//                CollectionUtils.isNotEmpty(this.newStargazersLogin);
//
//        // Deleted
//        this.deletedForksLogin = ListUtils.subtract(prevForksLogin, latestForksLogin);
//        this.deletedWatchersLogin = ListUtils.subtract(prevWatchersLogin, latestWatchersLogin);
//        this.deletedStargazersLogin = ListUtils.subtract(prevStargazersLogin, latestStargazersLogin);
//        this.isDeletedChanged = CollectionUtils.isNotEmpty(this.deletedForksLogin) || CollectionUtils.isNotEmpty(this.deletedWatchersLogin) ||
//                CollectionUtils.isNotEmpty(this.deletedStargazersLogin);
//
//        this.name = latestRepo.getName();
//    }
//
//}
