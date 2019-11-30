package org.occidere.githubnotifier.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author occidere
 * @since 2019-11-29
 */
@Getter
@Setter
@Document(
        indexName = "github-follow-info",
        shards = 5,
        replicas = 1,
        refreshInterval = "60s",
        createIndex = true
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
    @JsonProperty("avatar_url")
    private String avatarUrl;
    @JsonProperty("html_url")
    private String htmlUrl;
    private List<GithubFollower> followers;
}
