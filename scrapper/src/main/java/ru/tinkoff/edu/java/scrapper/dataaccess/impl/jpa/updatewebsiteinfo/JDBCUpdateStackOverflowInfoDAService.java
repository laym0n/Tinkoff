package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.updatewebsiteinfo;

import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JDBCStackOverflowInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JDBCTrackedLinkDAO;
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
