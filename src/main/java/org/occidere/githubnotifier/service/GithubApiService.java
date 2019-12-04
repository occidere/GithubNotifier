package org.occidere.githubnotifier.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.occidere.githubnotifier.vo.GithubFollower;
import org.occidere.githubnotifier.vo.GithubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

/**
 * @author occidere
 * @since 2019. 12. 02.
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
public class GithubApiService implements GithubApiRepository {

    @Autowired
    private RestTemplate restTemplate;

    @Value("#{jobParameters['githubApiToken'] == null ? '' : jobParameters['githubApiToken']}")
    private String githubApiToken;

    @Override
    public GithubUser getUser(String userId) {
        HttpEntity<GithubUser> httpEntity = null;
        if (StringUtils.isNotBlank(githubApiToken)) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(githubApiToken);
            httpEntity = new HttpEntity<>(null, headers);
        }

        ResponseEntity<GithubUser> entity = restTemplate.exchange(
                URI.create(GITHUB_API_URL + "/users/" + userId),
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<GithubUser>() {
                }
        );
        return entity.getBody();
    }

    @Override
    public List<GithubFollower> getFollowers(String userId) {
        final String url = GITHUB_API_URL + "/users/" + userId + "/followers?page=";

        HttpEntity<GithubUser> httpEntity = null;
        if (StringUtils.isNotBlank(githubApiToken)) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(githubApiToken);
            httpEntity = new HttpEntity<>(null, headers);
        }

        List<GithubFollower> followers = new ArrayList<>();
        for (int page = 1; ; page++) {
            final ResponseEntity<List<GithubFollower>> entity = restTemplate.exchange(
                    URI.create(url + page),
                    HttpMethod.GET,
                    httpEntity,
                    new ParameterizedTypeReference<List<GithubFollower>>() {
                    }
            );

            final List<GithubFollower> body = entity.getBody();
            if (CollectionUtils.isEmpty(body)) {
                break;
            } else {
                followers.addAll(body);
            }
        }

        return followers;
    }
}
