package org.occidere.githubnotifier.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.occidere.githubnotifier.vo.GithubFollower;
import org.occidere.githubnotifier.vo.GithubRepository;
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

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private RestTemplate restTemplate;

    @Value("${github.api.token:}")
    private String githubApiToken;

    @Override
    public GithubUser getUser(String login) {
        return MAPPER.convertValue(getRawData("/users/" + login), GithubUser.class);
    }

    @Override
    public List<GithubFollower> getFollowers(String login) {
        return getAllRawData("/users/" + login + "/followers").stream()
                .map(data -> MAPPER.convertValue(data, GithubFollower.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<GithubRepository> getRepositories(String login) {
        return getAllRawData("/users/" + login + "/repos").stream()
                .map(data -> MAPPER.convertValue(data, GithubRepository.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getStargazersLogins(String login, String repoName) {
        return getAllRawData("/repos/" + login + "/" + repoName + "/stargazers").stream()
                .map(data -> "" + data.get("login"))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getWatchersLogins(String login, String repoName) {
        return getAllRawData("/repos/" + login + "/" + repoName + "/watchers").stream()
                .map(data -> "" + data.get("login"))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getForksLogins(String login, String repoName) {
        return getAllRawData("/repos/" + login + "/" + repoName + "/forks").stream()
                .map(data -> "" + MAPPER.convertValue(data.get("owner"), Map.class).get("login"))
                .collect(Collectors.toList());
    }

    // TODO: refactoring
    private LinkedHashMap<String, Object> getRawData(String url) {
        HttpEntity<LinkedHashMap<String, Object>> httpEntity = null;
        if (StringUtils.isNotBlank(githubApiToken)) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(githubApiToken);
            httpEntity = new HttpEntity<>(null, headers);
        }

        final ResponseEntity<LinkedHashMap<String, Object>> entity = restTemplate.exchange(
                URI.create(GITHUB_API_URL + url),
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<LinkedHashMap<String, Object>>() {
                }
        );

        return entity.getBody();
    }

    // TODO: refactoring
    private List<LinkedHashMap<String, Object>> getAllRawData(String url) {
        HttpEntity<LinkedHashMap<String, Object>> httpEntity = null;
        if (StringUtils.isNotBlank(githubApiToken)) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(githubApiToken);
            httpEntity = new HttpEntity<>(null, headers);
        }

        List<LinkedHashMap<String, Object>> data = new ArrayList<>();
        for (int page = 1; ; page++) {
            final ResponseEntity<List<LinkedHashMap<String, Object>>> entity = restTemplate.exchange(
                    URI.create(GITHUB_API_URL + url + "?page=" + page),
                    HttpMethod.GET,
                    httpEntity,
                    new ParameterizedTypeReference<List<LinkedHashMap<String, Object>>>() {
                    }
            );

            final List<LinkedHashMap<String, Object>> body = entity.getBody();
            if (CollectionUtils.isEmpty(body)) {
                break;
            } else {
                data.addAll(body);
            }
        }

        return data;
    }
}
