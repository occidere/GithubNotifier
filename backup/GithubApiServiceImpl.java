//package org.occidere.githubnotifier.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.netty.channel.ChannelOption;
//import io.netty.handler.timeout.ReadTimeoutHandler;
//import io.netty.handler.timeout.WriteTimeoutHandler;
//import java.net.URI;
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.occidere.githubnotifier.vo.GithubFollower;
//import org.occidere.githubnotifier.vo.GithubRepository;
//import org.occidere.githubnotifier.vo.GithubUser;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.client.reactive.ReactorClientHttpConnector;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//import reactor.netty.http.client.HttpClient;
//import reactor.netty.tcp.TcpClient;
//import reactor.util.retry.Retry;
//
///**
// * @author occidere
// * @Blog: https://blog.naver.com/occidere
// * @Github: https://github.com/occidere
// * @since 2019. 12. 02.
// */
//@Slf4j
//@Service
//public class GithubApiServiceImpl implements GithubApiService {
//
//    private static final ObjectMapper MAPPER = new ObjectMapper();
//
//    @Value("${github.api.token:}")
//    private String githubApiToken;
//
//    private final WebClient webClient;
//
//    public GithubApiServiceImpl() {
//        this.webClient = WebClient.builder()
//                .clientConnector(
//                        new ReactorClientHttpConnector(
//                                HttpClient.from(
//                                        TcpClient.create()
//                                                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 60_000)
//                                                .doOnConnected(connection -> {
//                                                    connection.addHandlerLast(new ReadTimeoutHandler(60, TimeUnit.SECONDS));
//                                                    connection.addHandlerLast(new WriteTimeoutHandler(60, TimeUnit.SECONDS));
//                                                })
//                                )
//                        )
//                )
//                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0")
//                .build();
//    }
//
//    @Override
//    public GithubUser getUser(String login) {
//        return MAPPER.convertValue(getRawData("/users/" + login), GithubUser.class);
//    }
//
//    @Override
//    public List<GithubFollower> getFollowers(String login) {
//        return getAllRawData("/users/" + login + "/followers").stream()
//                .map(data -> MAPPER.convertValue(data, GithubFollower.class))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<GithubRepository> getRepositories(String login) {
//        return getAllRawData("/users/" + login + "/repos").stream()
//                .map(data -> MAPPER.convertValue(data, GithubRepository.class))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<String> getStargazersLogins(String login, String repoName) {
//        return getAllRawData("/repos/" + login + "/" + repoName + "/stargazers").stream()
//                .map(data -> "" + data.get("login"))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<String> getWatchersLogins(String login, String repoName) {
//        return getAllRawData("/repos/" + login + "/" + repoName + "/watchers").stream()
//                .map(data -> "" + data.get("login"))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<String> getForksLogins(String login, String repoName) {
//        return getAllRawData("/repos/" + login + "/" + repoName + "/forks").stream()
//                .map(data -> "" + MAPPER.convertValue(data.get("owner"), Map.class).get("login"))
//                .collect(Collectors.toList());
//    }
//
//    private LinkedHashMap<String, Object> getRawData(String url) {
//        return Objects.requireNonNull(getBody(url).block()).get(0);
//    }
//
//    private List<LinkedHashMap<String, Object>> getAllRawData(String url) {
//        List<LinkedHashMap<String, Object>> data = new ArrayList<>();
//        for (int page = 1; ; page++) {
//            List<LinkedHashMap<String, Object>> body = getBody(url + "?page=" + page).block();
//            if (CollectionUtils.isEmpty(body)) {
//                break;
//            } else {
//                data.addAll(body);
//            }
//        }
//        return data;
//    }
//
//    private Mono<List<LinkedHashMap<String, Object>>> getBody(final String uri) {
//        return webClient.get()
//                .uri(URI.create(GITHUB_API_URL + uri))
//                .headers(httpHeaders -> {
//                    if (StringUtils.isNotBlank(githubApiToken)) {
//                        httpHeaders.setBearerAuth(githubApiToken);
//                    }
//                })
//                .retrieve()
//                .bodyToFlux(new ParameterizedTypeReference<LinkedHashMap<String, Object>>() {
//                })
//                .collectList()
//                .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))) // will throw exception if failed
//                .defaultIfEmpty(new ArrayList<>());
//    }
//}
