package org.occidere.githubnotifier.service;

import java.util.List;
import org.occidere.githubnotifier.vo.GithubUser;

/**
 * @author occidere
 * @since 2019. 12. 02.
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
public interface GithubApiRepository {
    String GITHUB_API_URL = "https://api.github.com";
    String TOKEN = "b388bc7c097b3588bc94dd30389abcc5a6f2b38d";

    GithubUser getUser(String userId);

    List<String> getFollowers(String userId);
}
