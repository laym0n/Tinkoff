package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.chatdaservice.JDBCChatDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.checkupdatewebsiteinfo.JDBCCheckUpdateLinksDAServiceImpl;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.*;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.websiteinfochaindao.JDBCChainWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.websiteinfochaindao.JDBCChainWebsiteInfoDAOFactory;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.websiteinfochaindao.JDBCChainWebsiteInfoDAOFactoryImpl;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.trackedlinkdaservice.JDBCTrackedLinkDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.updatewebsiteinfo.JDBCUpdateGitHubInfoDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.updatewebsiteinfo.JDBCUpdateStackOverflowInfoDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPAChatDAO;

import javax.sql.DataSource;

@ConfigurationProperties
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JPAAccessConfiguration {
    @Bean
    public JPAChatDAO chatDAO(EntityManager entityManager){
        JPAChatDAO chatDAO =  new JPAChatDAO();
        chatDAO.setEntityManager(entityManager);
        return chatDAO;
    }
}
