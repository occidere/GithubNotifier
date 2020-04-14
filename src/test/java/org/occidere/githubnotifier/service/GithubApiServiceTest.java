package org.occidere.githubnotifier.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.occidere.githubnotifier.vo.GithubFollower;
import org.occidere.githubnotifier.vo.GithubRepository;
import org.occidere.githubnotifier.vo.GithubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-04-01
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        GithubApiServiceImpl.class
})
public class GithubApiServiceTest {

    @Autowired
    private GithubApiService service;

    @Test
    public void getUserTest() {
        // BUILD
        String login = "occidere";

        // OPERATE
        GithubUser res = service.getUser(login);

        // CHECK
        Assert.assertEquals(login, res.getLogin());
    }

    @Test
    public void getFollowersTest() {
        // BUILD
        String login = "marching0531";

        // OPERATE
        Set<String> followersLoginSet = service.getFollowers(login).stream()
                .map(GithubFollower::getLogin)
                .collect(Collectors.toSet());

        // CHECK
        Assert.assertTrue(followersLoginSet.contains("occidere"));
    }

    @Test
    public void getRepositoriesTest() {
        // BUILD
        String login = "occidere";

        // OPERATE
        Set<String> repositoriesNameSet = service.getRepositories(login).stream()
                .map(GithubRepository::getName)
                .collect(Collectors.toSet());

        // CHECK
        Assert.assertTrue(repositoriesNameSet.contains("GithubNotifier"));
    }

    @Test
    public void getStargazersLogins() {
        // BUILD
        String login = "occidere";
        String repoName = "GithubNotifier";

        // OPERATE
        Set<String> stargazersLoginSet = new HashSet<>(service.getStargazersLogins(login, repoName));

        // CHECK
        Assert.assertTrue(stargazersLoginSet.contains(login));
    }

    @Test
    public void getWatchersLogins() {
        // BUILD
        String login = "occidere";
        String repoName = "GithubNotifier";

        // OPERATE
        Set<String> watchersLoginSet = new HashSet<>(service.getWatchersLogins(login, repoName));

        // CHECK
        Assert.assertTrue(watchersLoginSet.contains(login));
    }

    @Test
    public void getForksLogins() {
        // BUILD
        String login = "occidere";
        String repoName = "common";

        // OPERATE
        Set<String> forksLoginSet = new HashSet<>(service.getForksLogins("NAVER-CAMPUS-HACKDAY-2017w", repoName));

        // CHECK
        Assert.assertTrue(forksLoginSet.contains(login));
    }
}
