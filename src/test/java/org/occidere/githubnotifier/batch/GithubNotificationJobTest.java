package org.occidere.githubnotifier.batch;

import static org.occidere.githubnotifier.enums.UpdateType.DELETE;
import static org.occidere.githubnotifier.enums.UpdateType.NEW;
import static org.occidere.githubnotifier.enums.UpdateType.NOT_CHANGE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.occidere.githubnotifier.configuration.GithubNotifierConfiguration;
import org.occidere.githubnotifier.enums.UpdateType;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
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
        GithubNotifierConfiguration.class,
        GithubNotificationJobTestConfiguration.class
})
//@SpringBatchTest
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
    public void groupingByTest() {
        // BUILD
        final Map<String, Integer> merged = new HashMap<>();
        merged.put("aaa", 0);
        merged.put("bbb", 1);
        merged.put("ccc", -1);

        // OPERATE
        Map<UpdateType, List<String>> groupby = merged.entrySet().stream()
                .map(e -> {
                    UpdateType updateType = e.getValue() == 0 ? NOT_CHANGE : (e.getValue() == -1 ? DELETE : NEW);
                    return Pair.of(updateType, e.getKey());
                })
                .collect(Collectors.groupingBy(Pair::getFirst, Collectors.mapping(Pair::getSecond, Collectors.toList())));

        // CHECK
        Assert.assertTrue(CollectionUtils.containsAny(groupby.get(DELETE), List.of("ccc")));
        Assert.assertTrue(CollectionUtils.containsAny(groupby.get(NEW), List.of("bbb")));
        Assert.assertTrue(CollectionUtils.containsAny(groupby.get(NOT_CHANGE), List.of("aaa")));
    }

    @Test
    public void jobTest() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
        ExitStatus exitStatus = jobLauncherTestUtils.launchJob(jobParameters).getExitStatus();

        Assert.assertEquals(ExitStatus.COMPLETED, exitStatus);
    }

}
