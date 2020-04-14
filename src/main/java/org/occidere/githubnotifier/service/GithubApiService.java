package org.occidere.githubnotifier.service;

import java.util.List;
import org.occidere.githubnotifier.vo.GithubFollower;
import org.occidere.githubnotifier.vo.GithubRepository;
import org.occidere.githubnotifier.vo.GithubUser;

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2019. 12. 02.
 */
public interface GithubApiService {

    String GITHUB_API_URL = "https://api.github.com";

    GithubUser getUser(String login);

    List<GithubFollower> getFollowers(String login);

    List<GithubRepository> getRepositories(String login);

    List<String> getStargazersLogins(String login, String repoName);

    List<String> getWatchersLogins(String login, String repoName);

    List<String> getForksLogins(String login, String repoName);
}
