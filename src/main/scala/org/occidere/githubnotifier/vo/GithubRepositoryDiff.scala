package org.occidere.githubnotifier.vo

import lombok.ToString

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020. 08. 15.
 */
@ToString
class GithubRepositoryDiff(prevRepo: GithubRepository, latestRepo: GithubRepository) {
  // New
  val newForksLogin: Iterable[String] = latestRepo.forksLogin.toList.diff(prevRepo.forksLogin.toList)
  val newWatchersLogin: Iterable[String] = latestRepo.watchersLogin.toList.diff(prevRepo.watchersLogin.toList)
  val newStargazersLogin: Iterable[String] = latestRepo.stargazersLogin.toList.diff(prevRepo.stargazersLogin.toList)
  val hasNewChanged: Boolean = newForksLogin.nonEmpty || newWatchersLogin.nonEmpty || newStargazersLogin.nonEmpty

  // Deleted
  val deletedForksLogin: Iterable[String] = prevRepo.forksLogin.toList.diff(latestRepo.forksLogin.toList)
  val deletedWatchersLogin: Iterable[String] = prevRepo.watchersLogin.toList.diff(latestRepo.watchersLogin.toList)
  val deletedStargazersLogin: Iterable[String] = prevRepo.stargazersLogin.toList.diff(latestRepo.stargazersLogin.toList)
  val hasDeletedChanged: Boolean = deletedForksLogin.nonEmpty || deletedWatchersLogin.nonEmpty || deletedStargazersLogin.nonEmpty

  val name: String = latestRepo.name
}
