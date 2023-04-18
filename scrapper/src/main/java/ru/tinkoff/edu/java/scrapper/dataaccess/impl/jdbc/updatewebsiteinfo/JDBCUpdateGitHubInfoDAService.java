package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.updatewebsiteinfo;

import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCGitHubInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCTrackedLinkDAO;
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
