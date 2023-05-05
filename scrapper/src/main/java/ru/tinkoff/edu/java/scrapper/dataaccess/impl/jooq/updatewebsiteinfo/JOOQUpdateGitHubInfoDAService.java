package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.updatewebsiteinfo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao.JOOQGitHubInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao.JOOQTrackedLinkDAO;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareGitHubInfo;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JOOQUpdateGitHubInfoDAService extends JOOQUpdateWebsiteInfo<ResultOfCompareGitHubInfo> {
    private JOOQGitHubInfoDAO gitHubInfoDAO;

    public JOOQUpdateGitHubInfoDAService(
        JOOQTrackedLinkDAO trackedLinkDAO,
        JOOQGitHubInfoDAO gitHubInfoDAO
    ) {
        super(trackedLinkDAO);
        this.gitHubInfoDAO = gitHubInfoDAO;
    }

    @Override
    public void applyChanges(ResultOfCompareGitHubInfo changes) {
        gitHubInfoDAO.applyChanges(changes);
    }
}
