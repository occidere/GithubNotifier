package org.occidere.githubnotifier.batch;

import java.util.List;
import org.occidere.githubnotifier.service.GithubUserService;
import org.occidere.githubnotifier.vo.GithubUser;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

/**
 * @author occidere
 * @since 2019-11-29
 */
public class GithubUserNotificationWriter implements ItemWriter<GithubUser>, StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private GithubUserService githubUserService;

    @Override
    public void write(List<? extends GithubUser> items) throws Exception {

    }
}
