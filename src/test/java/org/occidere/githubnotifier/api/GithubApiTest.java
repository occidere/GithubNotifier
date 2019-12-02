package org.occidere.githubnotifier.api;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.occidere.githubnotifier.configuration.GithubNotifierConfiguration;
import org.occidere.githubnotifier.service.GithubApiRepository;
import org.occidere.githubnotifier.service.GithubMonitoringTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author occidere
 * @since 2019. 11. 29.
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        GithubNotifierConfiguration.class,
})
public class GithubApiTest {

    @Autowired
    private GithubApiRepository githubApiRepository;

    @Autowired
    private GithubMonitoringTargetService monitoringTargetService;

    @Test
    public void getFollowerApiTest() {
        List<String> followers = githubApiRepository.getFollowers("occidere");
    }

    @Test
    public void getTargetUserIdTest() {
        List<String> logins = monitoringTargetService.findAllLogins();
        for (String login : logins) {
            System.out.println(login);
        }
    }
}
