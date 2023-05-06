package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.updatewebsiteinfo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPAGitHubInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPATrackedLinkDAO;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareGitHubInfo;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JPAUpdateGitHubInfoDAService extends JPAUpdateWebsiteInfo<ResultOfCompareGitHubInfo> {
    private JPAGitHubInfoDAO gitHubInfoDAO;

    public JPAUpdateGitHubInfoDAService(JPATrackedLinkDAO trackedLinkDAO, JPAGitHubInfoDAO gitHubInfoDAO) {
        super(trackedLinkDAO);
        this.gitHubInfoDAO = gitHubInfoDAO;
    }

    @Override
    public void applyChanges(ResultOfCompareGitHubInfo changes) {
        gitHubInfoDAO.applyChanges(changes);
    }
}
