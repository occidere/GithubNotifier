package org.occidere.githubnotifier.batch;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.occidere.githubnotifier.service.GithubMonitoringTargetService;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author occidere
 * @since 2019. 12. 02.
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
public class GithubUserFetchReader implements ItemReader<String>, StepExecutionListener {

    @Value("#{jobParameters[userId]}")
    private String userId;

    private Iterator<String> iter;

    @Autowired
    private GithubMonitoringTargetService service;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        iter = getMonitoringTargetUserIds().iterator();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    @Override
    public String read() throws Exception {
        if (iter.hasNext()) {
            return iter.next();
        } else {
            return null;
        }
    }

    private List<String> getMonitoringTargetUserIds() {
        return StringUtils.isNotBlank(userId) ? List.of(userId) : service.findAllLogins();
    }
}
