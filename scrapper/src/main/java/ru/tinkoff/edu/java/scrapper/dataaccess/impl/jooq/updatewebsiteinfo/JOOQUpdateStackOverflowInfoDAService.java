package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.updatewebsiteinfo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao.JOOQStackOverflowInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao.JOOQTrackedLinkDAO;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareStackOverflowInfo;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JOOQUpdateStackOverflowInfoDAService extends JOOQUpdateWebsiteInfo<ResultOfCompareStackOverflowInfo> {
    private JOOQStackOverflowInfoDAO stackOverflowDAO;

    public JOOQUpdateStackOverflowInfoDAService(
        JOOQTrackedLinkDAO trackedLinkDAO,
        JOOQStackOverflowInfoDAO stackOverflowDAO
    ) {
        super(trackedLinkDAO);
        this.stackOverflowDAO = stackOverflowDAO;
    }

    @Override
    public void applyChanges(ResultOfCompareStackOverflowInfo changes) {
        stackOverflowDAO.applyChanges(changes);
    }
}
