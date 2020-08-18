package org.occidere.githubnotifier.configuration

import java.util.Properties

import lombok.Value
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import org.springframework.data.elasticsearch.client.{ClientConfiguration, RestClients}
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020. 08. 16.
 */
@Configuration
@EnableElasticsearchRepositories
class ElasticsearchConfiguration extends AbstractElasticsearchConfiguration {
  private val props: Properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("/application.properties"))

//  @Value("${elasticsearch.endpoint:localhost:9200}")
  private var elasticsearchEndpoint: String = props.getProperty("elasticsearch.endpoint", "localhost:9200")

  override def elasticsearchClient: RestHighLevelClient = RestClients.create(ClientConfiguration.create(elasticsearchEndpoint)).rest
}
