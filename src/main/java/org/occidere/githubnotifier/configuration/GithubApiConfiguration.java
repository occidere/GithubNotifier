package org.occidere.githubnotifier.configuration;

import org.apache.http.impl.client.HttpClientBuilder;
import org.occidere.githubnotifier.service.GithubApiRepository;
import org.occidere.githubnotifier.service.GithubApiService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author sungjun.lee (occidere)
 * @since 2020-04-01
 */
@Configuration
public class GithubApiConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(6000);
        httpRequestFactory.setReadTimeout(6000);
        httpRequestFactory.setHttpClient(
                HttpClientBuilder.create()
                        .setMaxConnTotal(100)
                        .setMaxConnPerRoute(10)
                        .build());
        return new RestTemplate(httpRequestFactory);
    }

    @Bean
    public GithubApiRepository githubApiRepository() {
        return new GithubApiService();
    }
}
