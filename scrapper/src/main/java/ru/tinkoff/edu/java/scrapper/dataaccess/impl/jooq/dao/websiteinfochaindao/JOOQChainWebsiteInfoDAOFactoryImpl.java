package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao.websiteinfochaindao;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao.JOOQGitHubInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao.JOOQStackOverflowInfoDAO;

@AllArgsConstructor
@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JOOQChainWebsiteInfoDAOFactoryImpl implements JOOQChainWebsiteInfoDAOFactory {
    private JOOQGitHubInfoDAO gitHubInfoDAO;
    private JOOQStackOverflowInfoDAO stackOverflowInfoDAO;

    @Override
    public JOOQChainWebsiteInfoDAO getJOOQWebsiteInfoInfoDAO() {
        JOOQChainGitHubInfoDAOImpl
            chainGitHubInfoDAO = new JOOQChainGitHubInfoDAOImpl(gitHubInfoDAO, null);

        return new JOOQChainStackOverflowInfoDAOImpl(
            stackOverflowInfoDAO,
            chainGitHubInfoDAO
        );
    }
}
