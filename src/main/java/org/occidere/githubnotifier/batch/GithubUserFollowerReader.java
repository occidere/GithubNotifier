package org.occidere.githubnotifier.batch;

import java.util.Iterator;
import java.util.List;
import org.occidere.githubnotifier.api.GithubApi;
import org.occidere.githubnotifier.api.GithubFollowerApi;
import org.occidere.githubnotifier.vo.GithubUser;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;

/**
 * @author occidere
 * @since 2019-11-29
 */
public class GithubUserFollowerReader implements ItemReader<GithubUser>, StepExecutionListener {

    private Iterator<GithubUser> iterator;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        GithubUser user1 = new GithubUser();
        user1.setLogin("occidere");
        iterator = List.of(user1).iterator();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    @Override
    public GithubUser read() throws Exception {
        GithubApi githubApi = new GithubFollowerApi();

        return iterator.hasNext() ? iterator.next() : null;
    }
}
