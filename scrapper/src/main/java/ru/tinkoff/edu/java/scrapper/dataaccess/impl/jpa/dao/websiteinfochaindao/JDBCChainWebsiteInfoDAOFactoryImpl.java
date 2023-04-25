package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.websiteinfochaindao;

import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.*;

import javax.sql.DataSource;

public class JDBCChainWebsiteInfoDAOFactoryImpl implements JDBCChainWebsiteInfoDAOFactory {
    @Override
    public JPAChainWebsiteInfoDAO getJDBCWebsiteInfoInfoDAO() {
        JPAGitHubBranchesDAO branchesDAO = new JPAGitHubBranchesDAO();
        JPAGitHubCommitDAO commitsDAO = new JPAGitHubCommitDAO();
        JPAGitHubInfoDAO gitHubInfoDAO = new JPAGitHubInfoDAO(commitsDAO, branchesDAO);
        JDBCChainGitHubInfoDAOImpl chainGitHubInfoDAO = new JDBCChainGitHubInfoDAOImpl(gitHubInfoDAO, null);

        JPACStackOverflowCommentDAO commentDAO = new JPACStackOverflowCommentDAO();
        JPAStackOverflowAnswerDAO answerDAO = new JPAStackOverflowAnswerDAO();
        JPAStackOverflowInfoDAO stackOverflowInfoDAO = new JPAStackOverflowInfoDAO(answerDAO, commentDAO);
        JDBCChainStackOverflowInfoDAOImpl chainStackOverflowInfoDAO = new JDBCChainStackOverflowInfoDAOImpl(stackOverflowInfoDAO,
                chainGitHubInfoDAO);
        return chainStackOverflowInfoDAO;
    }
}
