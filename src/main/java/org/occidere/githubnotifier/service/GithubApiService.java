package org.occidere.githubnotifier.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.occidere.githubnotifier.vo.GithubUser;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public GithubUser getUser(String userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(TOKEN);
        HttpEntity<GithubUser> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<GithubUser> entity = restTemplate.exchange(
                URI.create(GITHUB_API_URL + "/users/" + userId + "?client_secret=" + TOKEN),
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<GithubUser>() {
                }
        );
        return entity.getBody();
    }

    @Override
    public List<String> getFollowers(String userId) {
        final String url = GITHUB_API_URL + "/users/" + userId + "/followers?page=";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(TOKEN);
        HttpEntity<GithubUser> httpEntity = new HttpEntity<>(null, headers);

        List<String> followers = new ArrayList<>();
        for (int page = 1; ; page++) {
            final ResponseEntity<List<LinkedHashMap<String, Object>>> entity = restTemplate.exchange(
                    URI.create(url + page),
                    HttpMethod.GET,
                    httpEntity,
                    new ParameterizedTypeReference<List<LinkedHashMap<String, Object>>>() {
                    }
            );

            final List<LinkedHashMap<String, Object>> body = entity.getBody();
            if (CollectionUtils.isEmpty(body)) {
                break;
            } else {
                followers.addAll(body.stream().map(lhm -> (String) lhm.get("login")).collect(Collectors.toList()));
            }
        }

        return followers;
    }
}
