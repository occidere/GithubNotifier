package org.occidere.githubnotifier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author occidere
 * @since 2019-11-29
 */
@Service
@RequiredArgsConstructor
public class GithubUserService {
    private final GithubUserRepository githubUserRepository;
}
