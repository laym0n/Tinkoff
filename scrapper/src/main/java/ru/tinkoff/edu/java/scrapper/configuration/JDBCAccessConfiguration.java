package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.chatdaservice.JDBCChatDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.checkupdatewebsiteinfo.JDBCCheckUpdateLinksDAServiceImpl;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.*;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.websiteinfochaindao.JDBCChainWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.websiteinfochaindao.JDBCChainWebsiteInfoDAOFactory;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.websiteinfochaindao.JDBCChainWebsiteInfoDAOFactoryImpl;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.trackedlinkdaservice.JDBCTrackedLinkDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.updatewebsiteinfo.JDBCUpdateGitHubInfoDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.updatewebsiteinfo.JDBCUpdateStackOverflowInfoDAService;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JDBCAccessConfiguration {
    @Bean
    public JDBCChatDAO jdbcChatDAO(DataSource dataSource){
        return new JDBCChatDAO(dataSource);
    }
    @Bean
    public JDBCChainWebsiteInfoDAOFactory jdbcChainWebsiteInfoDAOFactory(DataSource dataSource){
        return new JDBCChainWebsiteInfoDAOFactoryImpl(dataSource);
    }
    @Bean
    public JDBCChainWebsiteInfoDAO jdbcChainWebsiteInfoDAO(JDBCChainWebsiteInfoDAOFactory factory){
        return factory.getJDBCWebsiteInfoInfoDAO();
    }
    @Bean
    public JDBCGitHubCommitDAO jdbcGitHubCommitDAO(DataSource dataSource){
        return new JDBCGitHubCommitDAO(dataSource);
    }
    @Bean
    public JDBCGitHubBranchesDAO jdbcGitHubBranchesDAO(DataSource dataSource){
        return new JDBCGitHubBranchesDAO(dataSource);
    }
    @Bean
    public JDBCStackOverflowCommentDAO jdbcStackOverflowCommentDAO(DataSource dataSource){
        return new JDBCStackOverflowCommentDAO(dataSource);
    }
    @Bean
    public JDBCStackOverflowAnswerDAO jdbcStackOverflowAnswerDAO(DataSource dataSource){
        return new JDBCStackOverflowAnswerDAO(dataSource);
    }
    @Bean
    public JDBCStackOverflowInfoDAO jdbcStackOverflowInfoDAO(JDBCStackOverflowAnswerDAO answerDAO,
                                                             JDBCStackOverflowCommentDAO commentDAO,
                                                             DataSource dataSource){
        return new JDBCStackOverflowInfoDAO(dataSource, answerDAO, commentDAO);
    }
    @Bean
    public JDBCGitHubInfoDAO jdbcGitHubInfoDAO(DataSource dataSource, JDBCGitHubCommitDAO commitDAO,
                                               JDBCGitHubBranchesDAO branchesDAO){
        return new JDBCGitHubInfoDAO(dataSource, commitDAO, branchesDAO);
    }
    @Bean
    public JDBCTrackedLinkDAO jdbcTrackedLinkDAO(DataSource dataSource, JDBCChainWebsiteInfoDAO jdbcChainWebsiteInfoDAO){
        return new JDBCTrackedLinkDAO(dataSource, jdbcChainWebsiteInfoDAO);
    }
    @Bean
    public JDBCChatDAService jdbcChatDAService(JDBCChatDAO chatDAO){
        return new JDBCChatDAService(chatDAO);
    }
    @Bean
    public JDBCTrackedLinkDAService jdbcTrackedLinkDAService(JDBCChatDAO chatDAO, JDBCChainWebsiteInfoDAO websiteInfoDAO, JDBCTrackedLinkDAO trackedLinkDAO){
        return new JDBCTrackedLinkDAService(trackedLinkDAO, websiteInfoDAO, chatDAO);
    }
    @Bean
    public JDBCCheckUpdateLinksDAServiceImpl jdbcCheckUpdateLinksDAService(JDBCChainWebsiteInfoDAO websiteInfoDAO){
        return new JDBCCheckUpdateLinksDAServiceImpl(websiteInfoDAO);
    }
    @Bean
    public JDBCUpdateStackOverflowInfoDAService jdbcUpdateStackOverflowInfoDAService(JDBCStackOverflowInfoDAO jdbcStackOverflowInfoDAO, JDBCTrackedLinkDAO trackedLinkDAO){
        return new JDBCUpdateStackOverflowInfoDAService(trackedLinkDAO, jdbcStackOverflowInfoDAO);
    }
    @Bean
    public JDBCUpdateGitHubInfoDAService jdbcUpdateGitHubInfoDAService(JDBCTrackedLinkDAO trackedLink, JDBCGitHubInfoDAO gitHubInfoDAO){
        return new JDBCUpdateGitHubInfoDAService(trackedLink, gitHubInfoDAO);
    }
}
