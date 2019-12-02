package org.occidere.githubnotifier.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.occidere.githubnotifier.configuration.GithubNotifierConfiguration;
import org.occidere.githubnotifier.vo.GithubMonitoringTarget;
import org.occidere.githubnotifier.vo.GithubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

/**
 * @author occidere
 * @since 2019. 12. 02.
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        GithubNotifierConfiguration.class
})
public class GithubUserServiceTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private GithubUserRepository userRepository;

    @Autowired
    private GithubMonitoringTargetService githubMonitoringTargetService;

    @Test
    public void esFindTest() {
        // BUILD
        GithubUser user = new GithubUser();
        user.setLogin("occidere");
        user.setId(20942871);
        user.setNodeId("MDQ6VXNlcjIwOTQyODcx");
        user.setAvatarUrl("https://avatars3.githubusercontent.com/u/20942871?v=4");
        user.setHtmlUrl("https://github.com/occidere");

        // OPERATE
        GithubUser userFromEs = userRepository.findByLogin(user.getLogin());

        // CHECK
        System.out.println("USER1: " + user);
        System.out.println("USER2: " + userFromEs);
        Assert.assertEquals(user.getId(), userFromEs.getId());
    }

    @Test
    public void monitoringTargetEsIndexingTest() throws IOException {
        // BUILD
        GithubMonitoringTarget target1 = new GithubMonitoringTarget() {{
            setLogin("occidere");
            setId(20942871);
        }};
        GithubMonitoringTarget target2 = new GithubMonitoringTarget() {{
            setLogin("marching0531");
            setId(22049151);
        }};

        // OPERATE
        githubMonitoringTargetService.saveAll(List.of(target1, target2));

        // CHECK
        Iterator<String> logins = githubMonitoringTargetService.findAllLogins().iterator();
        Assert.assertTrue(CollectionUtils.contains(logins, target1.getLogin()));
        Assert.assertTrue(CollectionUtils.contains(logins, target2.getLogin()));
    }

}
