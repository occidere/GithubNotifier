package org.occidere.githubnotifier

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.context.ApplicationContext

/**
 * @author occidere
 * @since 2020. 08. 15.
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 */
object GithubNotifierApplication extends App {
  val context: ApplicationContext = SpringApplication.run(classOf[GithubNotifierApplication], args: _*)
  val exitCode = SpringApplication.exit(context, () => 0)
  System.exit(exitCode)
}

@SpringBootApplication(exclude = Array(classOf[DataSourceAutoConfiguration]))
class GithubNotifierApplication

