package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.websiteinfochaindao.JDBCChainWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.websiteinfochaindao.JDBCChainWebsiteInfoDAOFactory;

@ConfigurationProperties
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JDBCAccessConfiguration {
    @Bean
    public JDBCChainWebsiteInfoDAO jdbcChainWebsiteInfoDAO(JDBCChainWebsiteInfoDAOFactory factory) {
        return factory.getJDBCWebsiteInfoInfoDAO();
    }
}
