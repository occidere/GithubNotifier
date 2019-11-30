package org.occidere.githubnotifier.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.occidere.githubnotifier.configuration.GithubNotifierConfiguration;
import org.occidere.githubnotifier.vo.GithubFollower;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

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
@SpringBootTest
public class GithubApiTest {

	@Autowired
	@Qualifier("followerApi")
	private GithubApi followerApi;

	@Test
	public void getFollowerApiTest() {
		List<GithubFollower> followers = followerApi.getFollowers("marching0531");

	}
}
