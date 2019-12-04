package org.occidere.githubnotifier.service;

import java.util.List;
import org.occidere.githubnotifier.vo.GithubFollower;
import org.occidere.githubnotifier.vo.GithubUser;

/**
 * @author occidere
 * @since 2019. 12. 02.
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
public interface GithubApiRepository {
    String GITHUB_API_URL = "https://api.github.com";

    GithubUser getUser(String userId);

    List<GithubFollower> getFollowers(String userId);
}
