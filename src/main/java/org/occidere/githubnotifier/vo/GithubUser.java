package org.occidere.githubnotifier.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author occidere
 * @since 2019. 11. 29.
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
@Getter
@Setter
@Document(
        indexName = "github-users",
        shards = 5,
        replicas = 1,
        refreshInterval = "60s",
        createIndex = true,
        type = "_doc"
)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GithubUser {

    @Id
    private long id;
    @JsonProperty("node_id")
    private String nodeId;
    private String login;
    private String name;
    private String company;
    private String blog;
    private String location;
    private String email;
    private String bio;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    @JsonProperty("html_url")
    private String htmlUrl;
    private List<GithubFollower> followerList = new ArrayList<>();
    private List<GithubRepository> repositories = new ArrayList<>();
}
