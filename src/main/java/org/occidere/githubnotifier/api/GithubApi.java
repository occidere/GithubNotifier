package org.occidere.githubnotifier.api;

import java.util.List;
import org.occidere.githubnotifier.vo.GithubFollower;

/**
 * @author occidere
 * @since 2019-11-29
 */
public interface GithubApi {
    String GITHUB_API_ADDRESS = "https://api.github.com";

    List<GithubFollower> getFollowers();
}
