package org.occidere.githubnotifier.configuration

import java.util.Properties

import com.linecorp.bot.client.LineMessagingClient
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020. 08. 17.
 */
@Configuration
class LineBotConfiguration {
  private val props: Properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("/application.properties"))

  private val lineChannel: String = props.getProperty("line.channel.token")

  @Bean
  def lineMessagingClient: LineMessagingClient = LineMessagingClient.builder(lineChannel).build
}
