package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.websiteinfochaindao;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.*;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JPAChainWebsiteInfoDAOFactoryImpl implements JPAChainWebsiteInfoDAOFactory {
    @Override
    public JPAChainWebsiteInfoDAO getJPAWebsiteInfoInfoDAO() {
        JPAGitHubBranchesDAO branchesDAO = new JPAGitHubBranchesDAO();
        JPAGitHubCommitDAO commitsDAO = new JPAGitHubCommitDAO();
        JPAGitHubInfoDAO gitHubInfoDAO = new JPAGitHubInfoDAO(commitsDAO, branchesDAO);
        JPAChainGitHubInfoDAOImpl chainGitHubInfoDAO = new JPAChainGitHubInfoDAOImpl(gitHubInfoDAO, null);

        JPACStackOverflowCommentDAO commentDAO = new JPACStackOverflowCommentDAO();
        JPAStackOverflowAnswerDAO answerDAO = new JPAStackOverflowAnswerDAO();
        JPAStackOverflowInfoDAO stackOverflowInfoDAO = new JPAStackOverflowInfoDAO(answerDAO, commentDAO);
        JPAChainStackOverflowInfoDAOImpl chainStackOverflowInfoDAO = new JPAChainStackOverflowInfoDAOImpl(stackOverflowInfoDAO,
                chainGitHubInfoDAO);
        return chainStackOverflowInfoDAO;
    }
}
