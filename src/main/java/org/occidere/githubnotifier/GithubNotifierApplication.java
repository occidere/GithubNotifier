package org.occidere.githubnotifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GithubNotifierApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(GithubNotifierApplication.class, args);
        int exitCode = SpringApplication.exit(context, () -> 0);
        System.exit(exitCode);
    }
}