package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.updatewebsiteinfo;

import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JDBCGitHubInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JDBCTrackedLinkDAO;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareGitHubInfo;

public class JDBCUpdateGitHubInfoDAService extends JDBCUpdateWebsiteInfo<ResultOfCompareGitHubInfo> {
    private JDBCGitHubInfoDAO gitHubInfoDAO;

    public JDBCUpdateGitHubInfoDAService(JDBCTrackedLinkDAO trackedLinkDAO, JDBCGitHubInfoDAO gitHubInfoDAO) {
        super(trackedLinkDAO);
        this.gitHubInfoDAO = gitHubInfoDAO;
    }

    @Override
    public void applyChanges(ResultOfCompareGitHubInfo changes) {
        gitHubInfoDAO.applyChanges(changes);
    }
}
