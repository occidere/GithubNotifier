package org.occidere.githubnotifier.batch;

import java.util.List;
import java.util.stream.Collectors;
import org.occidere.githubnotifier.service.GithubUserRepository;
import org.occidere.githubnotifier.vo.GithubUser;
import org.occidere.githubnotifier.vo.GithubUserFollowerInfo;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author occidere
 * @since 2019. 12. 02.
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
public class GithubUserReflectWriter implements ItemWriter<GithubUserFollowerInfo>, StepExecutionListener {

    @Autowired
    private GithubUserRepository userRepository;

    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    @Override
    public void write(List<? extends GithubUserFollowerInfo> items) throws Exception {
        List<GithubUser> users = items.stream().map(GithubUserFollowerInfo::getGithubUser).collect(Collectors.toList());
        userRepository.saveAll(users);

        // TODO: Message send
    }
}
