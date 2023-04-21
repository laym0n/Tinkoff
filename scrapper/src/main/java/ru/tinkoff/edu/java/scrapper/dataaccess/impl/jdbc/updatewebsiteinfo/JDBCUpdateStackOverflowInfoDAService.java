package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.updatewebsiteinfo;

import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCStackOverflowInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCTrackedLinkDAO;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareStackOverflowInfo;


public class JDBCUpdateStackOverflowInfoDAService extends JDBCUpdateWebsiteInfo<ResultOfCompareStackOverflowInfo> {
    private JDBCStackOverflowInfoDAO stackOverflowDAO;

    public JDBCUpdateStackOverflowInfoDAService(JDBCTrackedLinkDAO trackedLinkDAO, JDBCStackOverflowInfoDAO stackOverflowDAO) {
        super(trackedLinkDAO);
        this.stackOverflowDAO = stackOverflowDAO;
    }

    @Override
    public void applyChanges(ResultOfCompareStackOverflowInfo changes) {
        stackOverflowDAO.applyChanges(changes);
    }
}
