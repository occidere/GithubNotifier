package org.occidere.githubnotifier.vo;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author occidere
 * @since 2019-11-29
 */
@Builder
@Document(
        indexName = "github-follow-info",
        shards = 5,
        replicas = 1,
        refreshInterval = "60s",
        createIndex = true
)
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GithubUser {

    @Id
    private long id;
    private String login;
    private String avatarUrl;
    private String htmlUrl;
    private List<GithubFollower> followers;
}
