package org.occidere.githubnotifier.batch;

import java.util.Iterator;
import java.util.List;
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
        iterator = List.of(
                GithubUser.builder().login("id1").build(),
                GithubUser.builder().login("id2").build(),
                GithubUser.builder().login("id3").build()
        ).iterator();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    @Override
    public GithubUser read() throws Exception {

        return iterator.hasNext() ? iterator.next() : null;
    }
}
