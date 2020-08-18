package org.occidere.githubnotifier.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020. 08. 15.
 */
@Service
@Autowired
class GithubUserService(githubUserRepository: GithubUserRepository)
