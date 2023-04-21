package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.websiteinfochaindao;

import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.*;

import javax.sql.DataSource;

@AllArgsConstructor
public class JDBCChainWebsiteInfoDAOFactoryImpl implements JDBCChainWebsiteInfoDAOFactory {
    private DataSource dataSource;
    @Override
    public JDBCChainWebsiteInfoDAO getJDBCWebsiteInfoInfoDAO() {
        JDBCGitHubBranchesDAO branchesDAO = new JDBCGitHubBranchesDAO(dataSource);
        JDBCGitHubCommitDAO commitsDAO = new JDBCGitHubCommitDAO(dataSource);
        JDBCGitHubInfoDAO gitHubInfoDAO = new JDBCGitHubInfoDAO(dataSource, commitsDAO, branchesDAO);
        JDBCChainGitHubInfoDAOImpl chainGitHubInfoDAO = new JDBCChainGitHubInfoDAOImpl(gitHubInfoDAO, null);

        JDBCStackOverflowCommentDAO commentDAO = new JDBCStackOverflowCommentDAO(dataSource);
        JDBCStackOverflowAnswerDAO answerDAO = new JDBCStackOverflowAnswerDAO(dataSource);
        JDBCStackOverflowInfoDAO stackOverflowInfoDAO = new JDBCStackOverflowInfoDAO(dataSource, answerDAO, commentDAO);
        JDBCChainStackOverflowInfoDAOImpl chainStackOverflowInfoDAO = new JDBCChainStackOverflowInfoDAOImpl(stackOverflowInfoDAO,
                chainGitHubInfoDAO);
        return chainStackOverflowInfoDAO;
    }
}