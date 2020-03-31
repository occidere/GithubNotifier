package org.occidere.githubnotifier.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author occidere
 * @since 2020. 03. 31.
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
@Getter
@Setter
@Document(
        indexName = "github-repos",
        shards = 5,
        replicas = 1,
        refreshInterval = "60s",
        createIndex = true,
        type = "_doc"
)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
public class GithubRepository {
    @Id
    private String id;
    private String name;
    private String ownerId;
    private String ownerLogin;
    private String description;
    @JsonProperty("stargazers_count")
    private int stargazersCount;
    @JsonProperty("watchers_count")
    private int watchersCount;
    @JsonProperty("forks_count")
    private int forksCount;

    private List<String> stargazersLogin = new ArrayList<>();
    private List<String> watchersLogin = new ArrayList<>();
    private List<String> forksLogin = new ArrayList<>();

    @JsonProperty("owner")
    private void unpackOwner(Map<String, Object> owner) {
        this.ownerId = String.valueOf(owner.get("id"));
        this.ownerLogin = (String) owner.get("login");
    }
}
