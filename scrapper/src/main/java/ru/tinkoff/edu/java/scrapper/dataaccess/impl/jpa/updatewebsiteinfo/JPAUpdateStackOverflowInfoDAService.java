package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.updatewebsiteinfo;

import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPAStackOverflowInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPATrackedLinkDAO;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareStackOverflowInfo;


public class JPAUpdateStackOverflowInfoDAService extends JPAUpdateWebsiteInfo<ResultOfCompareStackOverflowInfo> {
    private JPAStackOverflowInfoDAO stackOverflowDAO;

    public JPAUpdateStackOverflowInfoDAService(JPATrackedLinkDAO trackedLinkDAO, JPAStackOverflowInfoDAO stackOverflowDAO) {
        super(trackedLinkDAO);
        this.stackOverflowDAO = stackOverflowDAO;
    }

    @Override
    public void applyChanges(ResultOfCompareStackOverflowInfo changes) {
        stackOverflowDAO.applyChanges(changes);
    }
}
