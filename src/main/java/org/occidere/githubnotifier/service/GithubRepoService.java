package org.occidere.githubnotifier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author occidere
 * @since 2019. 11. 29.
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
@Service
@RequiredArgsConstructor
public class GithubRepoService {
    private final GithubRepoRepository githubRepoRepository;
}
