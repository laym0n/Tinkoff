package dataaccess.jpa;

import dataaccess.IntegrationEnvironment;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ScrapperApplication.class, JPAIntegrationEnvironment.RabbitMQTestConfig.class})
public class JPAIntegrationEnvironment extends IntegrationEnvironment {
    @TestConfiguration
    static class RabbitMQTestConfig {
        @MockBean
        public CachingConnectionFactory connectionFactory;
        @Bean
        public RabbitTemplate rabbitTemplate() {
            RabbitTemplate rabbitTemplateMock = mock(RabbitTemplate.class);
            when(rabbitTemplateMock.getConnectionFactory()).thenReturn(connectionFactory);
            return rabbitTemplateMock;
        }

    }
    @DynamicPropertySource
    static void jpaProperties(DynamicPropertyRegistry registry){
        registry.add("spring.liquibase.enabled", ()-> "false");
        registry.add("app.database-access-type", ()-> "jpa");
        registry.add("spring.datasource.url", singletonPostgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", singletonPostgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", singletonPostgreSQLContainer::getPassword);
        registry.add("spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation",
                ()->Boolean.TRUE);
        registry.add("spring.jpa.properties.hibernate.dialect",
                ()-> "org.hibernate.dialect.PostgreSQLDialect");
    }
}
