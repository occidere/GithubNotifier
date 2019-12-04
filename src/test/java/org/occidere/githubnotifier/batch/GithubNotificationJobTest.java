package org.occidere.githubnotifier.batch;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.occidere.githubnotifier.configuration.GithubNotifierConfiguration;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author occidere
 * @since 2019. 12. 02.
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        GithubNotifierConfiguration.class,
        GithubNotificationJobTestConfiguration.class
})
public class GithubNotificationJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void mergeTest() {
        // BUILD
        final Map<String, Integer> followersFromApi = new HashMap<>();
        followersFromApi.put("aaa", 1);
        followersFromApi.put("bbb", 1);

        final Map<String, Integer> followersFromPersistent = new HashMap<>();
        followersFromPersistent.put("aaa", -1);
        followersFromPersistent.put("ccc", -1);

        // OPERATE
        followersFromApi.forEach((k, v) -> followersFromPersistent.merge(k, v, Integer::sum));

        // CHECK
        Assert.assertEquals(0, (int) followersFromPersistent.get("aaa"));
        Assert.assertEquals(1, (int) followersFromPersistent.get("bbb"));
        Assert.assertEquals(-1, (int) followersFromPersistent.get("ccc"));
    }

    @Test
    public void jobTest() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
        ExitStatus exitStatus = jobLauncherTestUtils.launchJob(jobParameters).getExitStatus();

        Assert.assertEquals(ExitStatus.COMPLETED, exitStatus);
    }

}
