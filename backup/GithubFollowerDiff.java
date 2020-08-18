//package org.occidere.githubnotifier.vo;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import lombok.Getter;
//import lombok.ToString;
//
///**
// * @author occidere
// * @Blog: https://occidere.blog.me
// * @Github: https://github.com/occidere
// * @since 2020-04-14
// */
//@Getter
//@ToString
//public class GithubFollowerDiff {
//
//    private final List<GithubFollower> newFollowers = new ArrayList<>();
//    private final List<GithubFollower> deletedFollowers = new ArrayList<>();
//    private final List<GithubFollower> notChangedFollowers = new ArrayList<>();
//
//    // Grouping followers by NEW / DELETE / NOT_CHANGED
//    public GithubFollowerDiff(Map<String, GithubFollower> previousFollowers, Map<String, GithubFollower> latestFollowers) {
//        // New followers: followers who in latest follower list are not in previous one.
//        // Not changed followers: followers who in both previous and latest follower list.
//        for (Entry<String, GithubFollower> entry : latestFollowers.entrySet()) {
//            if (previousFollowers.containsKey(entry.getKey())) {
//                notChangedFollowers.add(entry.getValue());
//            } else {
//                newFollowers.add(entry.getValue());
//            }
//        }
//
//        // Deleted followers: followers who in previous follower list are not in latest one.
//        for (Entry<String, GithubFollower> entry : previousFollowers.entrySet()) {
//            if (!latestFollowers.containsKey(entry.getKey())) {
//                deletedFollowers.add(entry.getValue());
//            }
//        }
//    }
//}
