package org.occidere.githubnotifier.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author occidere
 * @since 2019-11-29
 */
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GithubFollower {
    private long id;
    private String login;
    private String avatarUrl;
    private String htmlUrl;
}
