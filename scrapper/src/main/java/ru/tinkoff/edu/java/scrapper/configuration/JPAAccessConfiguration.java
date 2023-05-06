package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.websiteinfochaindao.JPAChainWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.websiteinfochaindao.JPAChainWebsiteInfoDAOFactory;

@ConfigurationProperties
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JPAAccessConfiguration {
    @Bean
    public JPAChainWebsiteInfoDAO jdbcChainWebsiteInfoDAO(JPAChainWebsiteInfoDAOFactory factory){
        return factory.getJPAWebsiteInfoInfoDAO();
    }
}
