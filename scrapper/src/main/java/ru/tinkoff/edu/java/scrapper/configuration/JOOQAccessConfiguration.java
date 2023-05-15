package ru.tinkoff.edu.java.scrapper.configuration;

import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao.websiteinfochaindao.JOOQChainWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao.websiteinfochaindao.JOOQChainWebsiteInfoDAOFactory;

@ConfigurationProperties
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JOOQAccessConfiguration {
    @Bean
    public JOOQChainWebsiteInfoDAO jooqChainWebsiteInfoDAO(JOOQChainWebsiteInfoDAOFactory factory) {
        return factory.getJOOQWebsiteInfoInfoDAO();
    }

    @Bean
    public DefaultConfigurationCustomizer postgresJooqCustomizer() {
        return (DefaultConfiguration c) -> c.settings()
            .withRenderSchema(false)
            .withRenderFormatted(true)
            .withRenderQuotedNames(RenderQuotedNames.NEVER);
    }
}
