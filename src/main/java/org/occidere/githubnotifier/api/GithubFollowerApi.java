package org.occidere.githubnotifier.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.occidere.githubnotifier.vo.GithubFollower;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author occidere
 * @since 2019-11-29
 */
public final class GithubFollowerApi implements GithubApi {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<GithubFollower> getFollowers(String userId) {
        final String url = GITHUB_API_URL + "/users/" + userId + "/followers?page=";

        List<GithubFollower> followers = new ArrayList<>();
        for (int page = 1; ; page++) {
            final ResponseEntity<List<GithubFollower>> entity = restTemplate.exchange(
                URI.create(url + page),
                HttpMethod.GET,
                null,
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
