package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.websiteinfochaindao;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPAGitHubInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPAStackOverflowInfoDAO;

@Component
@AllArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JPAChainWebsiteInfoDAOFactoryImpl implements JPAChainWebsiteInfoDAOFactory {
    private JPAGitHubInfoDAO gitHubInfoDAO;
    private JPAStackOverflowInfoDAO stackOverflowInfoDAO;

    @Override
    public JPAChainWebsiteInfoDAO getJPAWebsiteInfoInfoDAO() {
        JPAChainGitHubInfoDAOImpl chainGitHubInfoDAO = new JPAChainGitHubInfoDAOImpl(gitHubInfoDAO, null);

        return new JPAChainStackOverflowInfoDAOImpl(stackOverflowInfoDAO,
                chainGitHubInfoDAO);
    }
}
